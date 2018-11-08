package me.mocha.blog.controller;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final PostRepository postRepository;

    @Value("${home.title}")
    private String title;

    @Value("${home.description}")
    private String description;

    @Autowired
    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/1";
    }

    @GetMapping("/{page}")
    public ModelAndView list(@PathVariable(value = "page") int page, ModelAndView mav) {
        List<Post> list = postRepository.findAll();
        list.sort((v1, v2) -> v2.getId() - v1.getId());

        mav.addObject("posts", list);

        if (list.size() > 0) {
            if (page < 1) return new ModelAndView("redirect:/1");
            int max = list.size() / 5 + (list.size() % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/" + max);
            int from = page * 5 - 5;
            int to = from + (max == page && list.size() % 5 != 0 ? list.size() % 5 : 5);
            list = list.subList(from, to);
        }

        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("home");
        mav.addObject("posts", list);
        return mav;
    }

}
