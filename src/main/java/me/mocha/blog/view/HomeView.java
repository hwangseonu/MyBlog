package me.mocha.blog.view;

import me.mocha.blog.model.entity.Post;
import me.mocha.blog.model.repository.PostRepository;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class HomeView {

    @Value("${main.title}")
    private String title;

    @Value("${main.description}")
    private String description;

    private final PostRepository postRepository;

    public HomeView(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping({"/", "/category"})
    public ModelAndView home(ModelAndView mav, @Param("page") Integer page) {
        if (page == null) page = 1;

        List<Post> list = postRepository.findAll();
        list.sort((v1, v2) -> v2.getId() - v1.getId());

        int size = list.size();

        if (size> 0) {
            if (page < 1) return new ModelAndView("redirect:/category/?page=1");
            int max = size / 5 + (size % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/category/?page=" + max);
            int from = page * 5 - 5;
            int to = from + (max == page && size % 5 != 0 ? size % 5 : 5);
            list = list.subList(from, to);
        }

        list.forEach(v -> {
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            v.setContent(Jsoup.parse(renderer.render(parser.parse(v.getContent()))).text());
        });

        mav.addObject("category", "home");
        mav.addObject("categories", Post.Category.categories());
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("posts", list);
        mav.addObject("pages", size / 5 - (size % 5 == 0 ? 1 : 0));
        mav.addObject("page", page);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/category/{category}")
    public ModelAndView listAboutCategory(@PathVariable("category") String category, @Param("page") Integer page, ModelAndView mav) {
        if (page == null) page = 1;
        if (!Post.Category.categories().contains(Post.Category.valueOf(category.toUpperCase()))) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 카테고리를 찾을 수 없습니다.");
            mav.addObject("categories", Post.Category.categories());
            mav.setViewName("error");
            return mav;
        }
        List<Post> list = postRepository.findAllByCategory(Post.Category.valueOf(category.toUpperCase()));
        int size = list.size();
        list.sort((v1, v2) -> v2.getId() - v1.getId());

        if (size > 0) {
            if (page < 1) return new ModelAndView("redirect:/category/" + category + "/?page=1");
            int max = size / 5 + (size % 5 == 0 ? 0 : 1);
            if (page > max) return new ModelAndView("redirect:/category/" + category + "/?page=" + max);
            int from = page * 5 - 5;
            int to = from + (max == page && size % 5 != 0 ? size % 5 : 5);
            list = list.subList(from, to);
        }

        list.forEach(v -> {
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            v.setContent(Jsoup.parse(renderer.render(parser.parse(v.getContent()))).text());
        });

        mav.addObject("category", category);
        mav.addObject("categories", Post.Category.categories());
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.addObject("posts", list);
        mav.addObject("pages", size / 5 - (size % 5 == 0 ? 1 : 0));
        mav.addObject("page", page);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/about")
    public ModelAndView about(ModelAndView mav) {
        mav.addObject("category", "about");
        mav.addObject("categories", Post.Category.categories());
        mav.addObject("title", title);
        mav.addObject("description", description);
        mav.setViewName("about");
        return mav;
    }

    @GetMapping("/editor")
    public ModelAndView newPost(ModelAndView mav) {
        mav.addObject("categories", Post.Category.categories());
        mav.setViewName("editor");
        return mav;
    }

    @GetMapping("/editor/{pid}")
    public ModelAndView newPost(@PathVariable("pid") Integer pid,  ModelAndView mav) {
        Post post = postRepository.findById(pid).orElse(null);

        if (post == null) {
            mav.setViewName("error");
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            return mav;
        }

        mav.addObject("post", post);
        mav.addObject("categories", Post.Category.categories());
        mav.setViewName("editor");
        return mav;
    }

    @GetMapping("/post/{pid}")
    public ModelAndView viewPost(ModelAndView mav, @PathVariable("pid") Integer pid) {
        Post post = postRepository.findById(pid).orElse(null);

        if (post == null) {
            mav.setStatus(HttpStatus.NOT_FOUND);
            mav.addObject("content", "해당 포스트를 찾을 수 없습니다.");
            mav.addObject("categories", Post.Category.categories());
            mav.setViewName("error");
            return mav;
        }

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        post.setContent(renderer.render(parser.parse(post.getContent())));

        mav.addObject("category", post.getCategory().toString().toLowerCase());
        mav.addObject("categories", Post.Category.categories());
        mav.addObject("post", post);
        mav.setViewName("post");
        return mav;
    }
}
