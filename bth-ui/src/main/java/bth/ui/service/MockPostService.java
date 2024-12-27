package bth.ui.service;

import bth.models.contract.PostService;
import bth.models.dto.PostDto;
import bth.models.dto.filter.PostsFilterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPostService implements PostService {

    public static final List<PostDto> POSTS = new ArrayList<>();
    public static final int POSTS_PAGE_SIZE = 5;

    private final RedisWrapper redisWrapper;

    @Override
    public List<PostDto> posts(int page, PostsFilterDto filter) {
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
    public PostDto post(String id) {
        return POSTS.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
