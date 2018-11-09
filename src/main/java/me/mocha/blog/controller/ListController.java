package me.mocha.blog.controller;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/list")
public class ListController {

    private final PostRepository postRepository;

    @Value("${main.title}")
    private String title;

    @Value("${main.description}")
    private String description;

    private static List<String> categories = new ArrayList<>();

    @Autowired
    public ListController(PostRepository postRepository) {
        this.postRepository = postRepository;
        categories.add(Post.Category.IT.toString());
        categories.add(Post.Category.SPRING.toString());
        categories.add(Post.Category.VLOG.toString());
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/list/all/1";
    }

    @GetMapping("/{page}")
    public String list(@PathVariable(value = "page") int page) {
        return "redirect:/list/all/"+page;
    }

    @GetMapping("/{category}/{page}")
    public ModelAndView listAboutCategory(@PathVariable("category") String category, @PathVariable("page") int page, ModelAndView mav) {
        List<Post> list;

        if (category.equals("all")) {
            list = postRepository.findAll();
        } else {
            if (!categories.contains(category)) {
                mav.setStatus(HttpStatus.NOT_FOUND);
                return mav;
            }
            list = postRepository.findAllByCategory(Post.Category.valueOf(category.toUpperCase()));
        }

        list.sort((v1, v2) -> v2.getId() - v1.getId());
        mav.addObject("posts", list);

        if (list.size() > 0) {
            if (page < 1) return new ModelAndView("redirect:/"+category+"/1");
            int max = list.size() / 5 + (list.size() % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/"+category+max);
            int from = page * 5 - 5;
            int to = from + (max == page && list.size() % 5 != 0 ? list.size() % 5 : 5);
            list = list.subList(from, to);
        }

        mav.addObject("categories", categories);
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("posts", list);
        mav.setViewName("list");
        return mav;
    }

}
