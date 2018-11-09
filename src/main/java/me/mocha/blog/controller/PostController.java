package me.mocha.blog.controller;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Value("${password}")
    private String password;

    private final PostRepository postRepository;

    private static List<String> categories = new ArrayList<>();

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
        categories.add(Post.Category.IT.toString());
        categories.add(Post.Category.SPRING.toString());
        categories.add(Post.Category.VLOG.toString());
    }

    @PostMapping("/")
    public ModelAndView create(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("category") String category, @RequestParam("password") String pw) {
        if (!pw.equals(password)) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("content", "패스워드를 정확히 입력하십시오.");
            mav.setStatus(HttpStatus.FORBIDDEN);
            return mav;
        }
        try {
            Post result = postRepository.save(Post.builder()
                    .title(title)
                    .content(content)
                    .createAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .category(Post.Category.valueOf(category.toUpperCase()))
                    .build());
            return new ModelAndView("redirect:/post/" + result.getId());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ModelAndView mav = new ModelAndView("error");
            mav.setStatus(HttpStatus.NOT_FOUND);
            return mav;
        }
    }

    @DeleteMapping("/{pid}")
    public ModelAndView delete(@PathVariable("pid") Integer pid, ModelAndView mav) {
        if (pid == null) {
            mav.setViewName("error");
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            mav.addObject("categories", categories);
            return mav;
        }
        return new ModelAndView("redirect:/");
    }

}
