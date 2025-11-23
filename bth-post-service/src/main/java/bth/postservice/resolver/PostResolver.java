package bth.postservice.resolver;

import bth.common.contract.PostService;
import bth.common.dto.PostDto;
import bth.common.dto.filter.PostsFilterDto;
import bth.common.exception.PostNotFoundException;
import bth.postservice.entity.Post;
import bth.postservice.entity.PostDocument;
import bth.postservice.mapper.PostDistanceMapper;
import bth.postservice.mapper.PostMapper;
import bth.postservice.repo.elastic.PostSearchRepository;
import bth.postservice.repo.jpa.PostsRepository;
import bth.postservice.repo.jpa.UserRepository;
import bth.postservice.service.NotificationSender;
import bth.postservice.service.PostGeneratorService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostResolver implements PostService {
    private static final int BATCH_SIZE = 50; // TODO: Fix JPA with join + pageable

    private final PostsRepository postsRepository;
    private final PostSearchRepository postSearchRepository;
    private final UserRepository userRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final PostMapper postMapper;
    private final PostDistanceMapper postDistanceMapper;
    private final PostGeneratorService postGeneratorService;
    private final NotificationSender notificationSender;
    @Value("${bth.post-service.elastic-search:false}")
    private boolean elasticSearch;

    @Override
    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var sw = StopWatch.createStarted();
        if (StringUtils.isNotEmpty(filter.getQuery()) && elasticSearch) {
            var query = filter.getQuery();
            var postDocuments = postSearchRepository.searchPosts(query, BATCH_SIZE, elasticsearchClient);
            log.debug("Elastic Search results for query: {}, posts {}", query, postDocuments.size());
            var postIds = postDocuments.stream().map(PostDocument::getId).toList();
            var postsById = postsRepository.findAllById(postIds).stream()
                    .collect(Collectors.toMap(Post::getId, post -> post));
            return postIds.stream()
                    .map(postId -> postsById.get(postId.toString()))
                    .filter(Objects::nonNull)
                    .map(postMapper::toDto)
                    .toList();
        }
        if (StringUtils.isNotEmpty(filter.getQuery())) {
            var posts = postsRepository.searchPosts(buildSearchQuery(filter.getQuery(), '|'), BATCH_SIZE, page);
            log.debug("GIN Search results for query: {}, posts {}", filter.getQuery(), posts.size());
            return posts.stream().map(postMapper::toDto).toList();
        }
        var postsPage = postsRepository.findFilteredPosts(filter, Collections.emptyList(), page, BATCH_SIZE);
        sw.stop();
        log.debug("Successfully loaded {} posts, total size: {}, {} ms",
                postsPage.getNumberOfElements(),
                postsPage.getTotalElements(),
                sw.getTime(TimeUnit.MILLISECONDS));
        return postsPage.stream()
                .map(postMapper::toDto)
                .toList();
    }

    private String buildSearchQuery(String query, char operator) {
        return Arrays.stream(query.split(" "))
                .map(String::trim)
                .collect(Collectors.joining(" %s ".formatted(operator)));
    }

    @Override
    @QueryMapping
    public List<PostDto> nearestPosts(@Argument("longitude") double longitude,
                                      @Argument("latitude") double latitude) {
        return postsRepository.findAllPostsOrderedByDistance(longitude, latitude).stream()
                .map(postDistanceMapper::toDto)
                .toList();
    }

    @Override
    @QueryMapping
    public PostDto post(@Argument("id") String id) {
        return postMapper.toDto(
                postsRepository.findById(UUID.fromString(id)).orElseThrow(() -> new PostNotFoundException(id)));
    }

    @Override
    @MutationMapping
    @Transactional
    public PostDto createPost(@Argument("imageUrl") String imageUrl,
                              @Argument("userId") String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PostNotFoundException("User not exist %s".formatted(username)));
        var post = postGeneratorService.generate();
        post.setUser(user);
        post.setImageUrl(imageUrl);
        var savedPost = postsRepository.save(post);
        postSearchRepository.save(createPostDocument(savedPost)); // save to elastic
        notificationSender.notifySubscribers(savedPost);
        return postMapper.toDto(savedPost);
    }

    private PostDocument createPostDocument(Post post) {
        return new PostDocument(
                UUID.fromString(post.getId()),
                post.getTitle(),
                post.getDetails().getDescription()
        );
    }

}
