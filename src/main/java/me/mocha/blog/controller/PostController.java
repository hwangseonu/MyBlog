package me.mocha.blog.controller;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/new")
    public ModelAndView newPost(ModelAndView mav) {
        List<Post.Category> categories = new ArrayList<>();
        categories.add(Post.Category.IT);
        categories.add(Post.Category.SPRING);
        categories.add(Post.Category.VLOG);
        mav.addObject("categories", categories);
        mav.setViewName("post/new");
        return mav;
    }

    @PostMapping("/")
    public String create(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("category") String category) {
        try {
            Post result = postRepository.save(Post.builder()
                    .title(title)
                    .content(content)
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .category(Post.Category.valueOf(category.toUpperCase()))
                    .build());
            return "redirect:/post/" + result.getId();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }



}
