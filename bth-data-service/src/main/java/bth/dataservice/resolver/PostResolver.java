package bth.dataservice.resolver;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
import bth.models.exception.PostNotFoundException;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

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
    public List<PostDto> posts(@Argument("page") int page, @Argument("filter") PostsFilterDto filter) {
        var filteredPosts = filterPosts(filter);
        if (page < 0) {
            return filteredPosts;
        }
        int start = page * BATCH_SIZE;
        int end = Math.min(start + BATCH_SIZE, filteredPosts.size());
        if (start >= filteredPosts.size()) {
            return Collections.emptyList();
        }
        var postDtos = filteredPosts.subList(start, end);
        log.debug("Successfully loaded {} posts", postDtos.size());
        return postDtos;
    }

    private List<PostDto> filterPosts(PostsFilterDto filter) {
        if (!filter.isNotEmpty()) {
            return POSTS;
        }
        return POSTS.stream() // TODO: Enhance filters but later with DB integration
                .filter(p -> ObjectUtils.nullSafeEquals(p.getLocation().getCity(), filter.getCity())
//                        && ObjectUtils.nullSafeEquals(p.getLocation().getCountry(), filter.getCountry())
//                        && isWithinRange(p.getDetails().getPrice(), filter.getPriceMin(), filter.getPriceMax())
                )
                .toList();
    }

    private boolean isWithinRange(Integer price, Integer min, Integer max) {
        if (price == null) return false;
        if (min != null && price < min) return false;
        if (max != null && price > max) return false;
        return true;
    }

    @QueryMapping
    public PostDto post(@Argument("id") String id) {
        return POSTS.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(id));
    }
}
