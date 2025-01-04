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
import bth.postservice.service.NotificationSender;
import bth.postservice.service.PostGeneratorService;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostResolver implements PostService {
    private static final int BATCH_SIZE = 30;

    private final PostsRepository postsRepository;
    private final PostSearchRepository postSearchRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final PostMapper postMapper;
    private final PostDistanceMapper postDistanceMapper;
    private final PostGeneratorService postGeneratorService;
    private final NotificationSender notificationSender;

    @Override
    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var sw = StopWatch.createStarted();
        List<UUID> postIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(filter.getQuery())) {
            var query = filter.getQuery();
            var postDocumentsFuzzy = postSearchRepository.findFuzzy(query, elasticsearchClient);
            var postDocumentsMultimatch = postSearchRepository.findMultimatch(query, elasticsearchClient);
            log.debug("Search fuzzy results for query: {}, posts {}", query, postDocumentsFuzzy.size());
            log.debug("Search multimatch results for query: {}, posts {}", query, postDocumentsMultimatch.size());
            postIds = postDocumentsMultimatch.stream().map(PostDocument::getId).toList();
        }
        var postsPage = postsRepository.findFilteredPosts(filter, postIds, page, BATCH_SIZE);
        sw.stop();
        log.debug("Successfully loaded {} posts, total size: {}, {} ms",
                postsPage.getNumberOfElements(),
                postsPage.getTotalElements(),
                sw.getTime(TimeUnit.MILLISECONDS));
        return postsPage.stream()
                .map(postMapper::toDto)
                .toList();
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
                              @Argument("userId") String userId) {
        var post = postGeneratorService.generate();
        post.setUserId(userId);
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
