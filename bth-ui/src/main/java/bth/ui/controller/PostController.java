package bth.ui.controller;

import bth.models.contract.PostService;
import bth.models.dto.filter.PostsFilterDto;
import bth.ui.service.RedisWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final RedisWrapper redisWrapper;

    @GetMapping("/posts")
    public String getPosts(@ModelAttribute PostsFilterDto filter, Model model) {
        var page = Integer.parseInt(redisWrapper.getOrDefault("postsPage", 0));
        model.addAttribute("posts", postService.posts(page, filter));
        model.addAttribute("filter", filter);
        return "post/posts";
    }

    @SneakyThrows
    @GetMapping("/posts/load")
    public String loadPosts(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                            Model model) {
        TimeUnit.SECONDS.sleep(1); // TODO: Emulate long loading
        model.addAttribute("posts", postService.posts(page, new PostsFilterDto())); // TODO: Get filters from AJAX
        return "fragments :: postList";
    }

    @GetMapping("/posts/details/{id}")
    public String getPosts(@PathVariable("id") String id, Model model) {
        model.addAttribute("post", postService.post(id));
        return "post/post";
    }
}
