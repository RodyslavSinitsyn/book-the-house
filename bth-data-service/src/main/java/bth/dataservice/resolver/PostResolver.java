package bth.dataservice.resolver;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class PostResolver implements PostService {
    public static final List<PostDto> POSTS = new ArrayList<>();
    public static final int BATCH_SIZE = 5;

    static {
        for (int i = 1; i <= 25; i++) {
            POSTS.add(new PostDto(UUID.randomUUID().toString(), "Post#" + i, "Description#" + i, "$49.99", "https://via.placeholder.com/150"));
        }
    }

    @QueryMapping
    public List<PostDto> posts(@Argument("page") int page) {
        if (page <= 0) {
            return POSTS;
        }
        int start = page * BATCH_SIZE;
        int end = Math.min(start + BATCH_SIZE, POSTS.size());
        if (start >= POSTS.size()) {
            return Collections.emptyList();
        }
        var postDtos = POSTS.subList(start, end);
        log.debug("Successfully loaded {} posts", postDtos.size());
        return postDtos;
    }

    @QueryMapping
    public PostDto post(@Argument("id") String id) {
        return POSTS.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}
