package me.mocha.blog.view;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeView {

    @Value("${main.title}")
    private String title;

    @Value("${main.description}")
    private String description;

    private final PostRepository postRepository;

    private static List<String> categories = new ArrayList<>();

    public HomeView(PostRepository postRepository) {
        this.postRepository = postRepository;
        categories.add(Post.Category.IT.toString());
        categories.add(Post.Category.SPRING.toString());
        categories.add(Post.Category.VLOG.toString());
    }

    @GetMapping({"/", "/category"})
    public ModelAndView home(ModelAndView mav, @Param("page") Integer page) {
        if (page == null) page = 1;

        List<Post> list = postRepository.findAll();
        list.sort((v1, v2) -> v2.getId() - v1.getId());

        if (list.size() > 0) {
            if (page < 1) return new ModelAndView("redirect:/category/?page=1");
            int max = list.size() / 5 + (list.size() % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/category/?page=" + max);
            int from = page * 5 - 5;
            int to = from + (max == page && list.size() % 5 != 0 ? list.size() % 5 : 5);
            list = list.subList(from, to);
        }

        mav.addObject("category", "home");
        mav.addObject("categories", categories);
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("posts", list);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/category/{category}")
    public ModelAndView listAboutCategory(@PathVariable("category") String category, @Param("page") Integer page, ModelAndView mav) {
        if (page == null) page = 1;
        if (!categories.contains(category)) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 카테고리를 찾을 수 없습니다.");
            mav.addObject("categories", categories);
            mav.setViewName("error");
            return mav;
        }
        List<Post> list = postRepository.findAllByCategory(Post.Category.valueOf(category.toUpperCase()));
        list.sort((v1, v2) -> v2.getId() - v1.getId());

        if (list.size() > 0) {
            if (page < 1) return new ModelAndView("redirect:/category/" + category + "/?page=1");
            int max = list.size() / 5 + (list.size() % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/category/" + category + "/?page=" + max);
            int from = page * 5 - 5;
            int to = from + (max == page && list.size() % 5 != 0 ? list.size() % 5 : 5);
            list = list.subList(from, to);
        }

        mav.addObject("category", category);
        mav.addObject("categories", categories);
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("posts", list);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/about")
    public ModelAndView about(ModelAndView mav) {
        mav.addObject("category", "about");
        mav.addObject("categories", categories);
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("about");
        return mav;
    }

    @GetMapping("/editor")
    public ModelAndView newPost(ModelAndView mav) {
        mav.addObject("categories", categories);
        mav.setViewName("editor");
        return mav;
    }

    @GetMapping("/post/{pid}")
    public ModelAndView viewPost(ModelAndView mav, @PathVariable("pid") Integer pid) {
        Post post = postRepository.findById(pid).orElse(null);

        if (post == null) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            mav.addObject("categories", categories);
            mav.setViewName("error");
            return mav;
        }

        mav.addObject("category", post.getCategory().toString().toLowerCase());
        mav.addObject("categories", categories);
        mav.addObject("post", post);
        mav.setViewName("post");
        return mav;
    }
}
