package bth.postservice.resolver;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
import bth.models.exception.PostNotFoundException;
import bth.postservice.mapper.PostMapper;
import bth.postservice.repo.PostsRepository;
import bth.postservice.service.PostGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostResolver implements PostService {
    private static final int BATCH_SIZE = 6;

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;
    private final PostGeneratorService postGeneratorService;

    @Override
    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var postsPage = postsRepository.findFilteredPosts(filter, page, BATCH_SIZE);
        log.debug("Successfully loaded {} posts, total size: {}", postsPage.getNumberOfElements(), postsPage.getTotalElements());
        return postsPage.stream()
                .map(postMapper::toDto)
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
    public PostDto createPost(@Argument("imageUrl") String imageUrl) {
        var post = postGeneratorService.generate();
        post.setImageUrl(imageUrl);
        var savedPost = postsRepository.save(post);
        return postMapper.toDto(savedPost);
    }
}
