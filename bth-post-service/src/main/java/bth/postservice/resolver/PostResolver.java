package bth.postservice.resolver;

import bth.postservice.mapper.PostMapper;
import bth.postservice.repo.PostsRepository;
import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
import bth.models.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PostResolver implements PostService {
    private static final int BATCH_SIZE = 5;

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;

    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var postsPage = postsRepository.findFilteredPosts(filter, page, BATCH_SIZE);
        log.debug("Successfully loaded {} posts, total size: {}", postsPage.getTotalPages(), postsPage.getTotalElements());
        return postsPage.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @QueryMapping
    public PostDto post(@Argument("id") String id) {
        return postMapper.toDto(
                postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
    }
}
