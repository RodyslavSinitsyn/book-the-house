package bth.ui.controller;

import bth.ui.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.getPosts(1));
        return "posts";
    }

    @SneakyThrows
    @GetMapping("/posts/load")
    public String loadPosts(@RequestParam(name = "page", defaultValue = "1", required = false) int page,
                           Model model) {
        TimeUnit.SECONDS.sleep(1); // TODO: Emulate long loading
        model.addAttribute("posts", postService.getPosts(page));
        return "fragments :: postList";
    }

    @GetMapping("/posts/details/{id}")
    public String getPosts(@PathVariable("id") String id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post";
    }
}
