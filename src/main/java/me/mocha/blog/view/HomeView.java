package me.mocha.blog.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class HomeView {

    @Value("${home.title}")
    private String title;

    @Value("${home.description}")
    private String description;

    @RequestMapping("/")
    public ModelAndView home(ModelAndView mav) {
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("home");
        return mav;
    }

    @RequestMapping("/about")
    public ModelAndView about(ModelAndView mav) {
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("about");
        return mav;
    }

}
