package me.mocha.blog.model.repository;

import me.mocha.blog.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCategory(Post.Category category);
}
