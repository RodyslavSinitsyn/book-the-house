package bth.dataservice.resolver;

import bth.dataservice.mapper.PostMapper;
import bth.dataservice.repo.PostsRepository;
import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
import bth.models.exception.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostResolver implements PostService {
    private static final Logger log = LoggerFactory.getLogger(PostResolver.class);
    private static final int BATCH_SIZE = 5;

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;

    public PostResolver(PostsRepository postsRepository, PostMapper postMapper) {
        this.postsRepository = postsRepository;
        this.postMapper = postMapper;
    }

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
