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

@Controller
@RequestMapping("/post")
public class PostController {

    @Value("${password}")
    private String password;

    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping("/")
    public ModelAndView create(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("category") String category, @RequestParam("password") String pw) {
        if (!pw.equals(password)) {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("content", "패스워드를 정확히 입력하십시오.");
            mav.addObject("categories", Post.Category.categories());
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
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("content", "카태고리가 잘못되었습니다.");
            mav.addObject("categories", Post.Category.categories());
            mav.setStatus(HttpStatus.NOT_FOUND);
            return mav;
        }
    }

    @DeleteMapping("/{pid}")
    public ModelAndView delete(@PathVariable("pid") Integer pid, ModelAndView mav, @RequestParam("password") String pw) {
        if (!pw.equals(password)) {
            mav.setViewName("error");
            mav.setStatus(HttpStatus.FORBIDDEN);
            mav.addObject("content", "패스워드를 정확히 입력해 주세요.");
            return mav;
        }

        Post post = postRepository.findById(pid).orElse(null);

        if (post == null) {
            mav.setViewName("error");
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            return mav;
        }

        postRepository.delete(post);
        return new ModelAndView("redirect:/");
    }

    @PatchMapping("/{pid}")
    public ModelAndView edit(@PathVariable("pid") Integer pid, @RequestParam("category") String category,
                             @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("password") String pw) {
        if (!pw.equals(password)) {
            ModelAndView mav = new ModelAndView("error");
            mav.setStatus(HttpStatus.FORBIDDEN);
            mav.addObject("categories", Post.Category.categories());
            mav.addObject("content", "패스워드를 정확히 입력해 주세요.");
            return mav;
        }

        Post post = postRepository.findById(pid).orElse(null);

        if (post == null) {
            ModelAndView mav = new ModelAndView("error");
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("categories", Post.Category.categories());
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            return mav;
        }

        post.setTitle(title);
        post.setContent(content);
        post.setCategory(Post.Category.valueOf(category.toUpperCase()));
        postRepository.save(post);
        return new ModelAndView("redirect:/post/"+post.getId());
    }

}
