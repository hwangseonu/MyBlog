package me.mocha.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AboutController {

    @Value("${home.title}")
    private String title;

    @Value("${home.description}")
    private String description;

    @GetMapping("/about")
    public ModelAndView about(ModelAndView mav) {
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("about");
        return mav;
    }

}
