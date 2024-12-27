package bth.dataservice.resolver;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.exception.PostNotFoundException;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
public class PostResolver implements PostService {
    public static final List<PostDto> POSTS = new ArrayList<>();
    public static final int BATCH_SIZE = 5;

    private static final EnhancedRandom RANDOM = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .stringLengthRange(1, 10)
            .build();

    static {
        for (int i = 1; i <= 50; i++) {
            POSTS.add(generateRandomPost());
        }
    }

    private static PostDto generateRandomPost() {
        var postDto = RANDOM.nextObject(PostDto.class);
        postDto.setImageUrl("https://via.placeholder.com/150");
        return postDto;
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
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}
