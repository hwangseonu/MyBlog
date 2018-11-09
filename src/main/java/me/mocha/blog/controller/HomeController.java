package me.mocha.blog.controller;

import me.mocha.blog.model.entity.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Value("${main.title}")
    private String title;

    @Value("${main.description}")
    private String description;

    @GetMapping
    public ModelAndView home(ModelAndView mav) {
        List<String> categories = new ArrayList<>();
        categories.add(Post.Category.IT.toString());
        categories.add(Post.Category.SPRING.toString());
        categories.add(Post.Category.VLOG.toString());

        mav.addObject("categories", categories);
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("home");
        return mav;
    }

}
