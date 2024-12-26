package bth.ui.service;

import bth.ui.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPostService implements PostService {

    public static final List<PostDto> POSTS = new ArrayList<>();
    public static final int POSTS_PAGE_SIZE = 5;

    private final RedisWrapper redisWrapper;

    static {
        for (int i = 1; i <= 25; i++) {
            POSTS.add(new PostDto(UUID.randomUUID().toString(), "Post#" + i, "Description#" + i, "$49.99", "https://via.placeholder.com/150"));
        }
    }

    @Override
    public List<PostDto> getPosts(int page) {
        // save page to cache
        redisWrapper.set("postsPage", String.valueOf(page), 30);

        if (page <= 0) {
            return POSTS;
        }
        int start = page * POSTS_PAGE_SIZE;
        int end = Math.min(start + POSTS_PAGE_SIZE, POSTS.size());
        if (start >= POSTS.size()) {
            return Collections.emptyList();
        }
        return POSTS.subList(start, end);
    }

    @Override
    public PostDto getPost(String id) {
        return POSTS.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
