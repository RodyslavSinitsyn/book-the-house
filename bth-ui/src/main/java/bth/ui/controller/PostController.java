package bth.ui.controller;

import bth.common.contract.PostService;
import bth.common.dto.PostDto;
import bth.common.dto.filter.PostsFilterDto;
import bth.ui.service.FacadeService;
import bth.ui.service.RedisWrapper;
import bth.ui.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final FacadeService facadeService;
    private final RedisWrapper redisWrapper;

    @GetMapping("/posts")
    public String getPosts(@ModelAttribute PostsFilterDto filter, Model model) {
        var page = Integer.parseInt(redisWrapper.getOrDefault("postsPage", 0));
        var postList = postService.posts(page, filter);
        model.addAttribute("posts", postList);
        model.addAttribute("filter", filter);
        redisWrapper.globalSetListWithTtlCheck("posts_" + page,
                postList,
                Duration.ofMinutes(5));
        return "post/posts";
    }

    @GetMapping("/posts/nearest")
    public String getPosts(@RequestParam("longitude") double longitude,
                           @RequestParam("latitude") double latitude,
                           Model model) {
        var postList = postService.nearestPosts(longitude, latitude);
        model.addAttribute("posts", postList);
        model.addAttribute("filter", PostsFilterDto.EMPTY);
        return "post/posts";
    }

    @SneakyThrows
    @GetMapping("/posts/load")
    public String loadPosts(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                            Model model) {
//         TODO: Get filters from AJAX
        var postList = postService.posts(page, PostsFilterDto.EMPTY);
        model.addAttribute("posts", postList);
        redisWrapper.globalSetListWithTtlCheck("posts_" + page,
                postList,
                Duration.ofMinutes(5));
        return "fragments :: postList";
    }

    @GetMapping("/posts/details/{id}")
    public String getPosts(@PathVariable("id") String id, Model model) {
        PostDto post = postService.post(id);
        model.addAttribute("post", post);
        model.addAttribute("authenticatedUserId", SessionUtils.getUsername());
        model.addAttribute("isAuth", SessionUtils.isAuthenticated());
        model.addAttribute("chatId", getChatId(post.getUserId()));
        return "post/post";
    }

    private String getChatId(String postCreator) {
        return Optional.ofNullable(redisWrapper.getJedis().hget(redisWrapper.getCacheKey("chat"), postCreator))
                .orElse(UUID.randomUUID().toString());
    }

    @PostMapping("/post")
    public String post(@RequestParam("file") MultipartFile file, Model model) {
        facadeService.createPost(file);
        return "redirect:/posts";
    }
}
