package me.mocha.blog.model.repository;

import me.mocha.blog.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByCategory(Post.Category category);
    Optional<Post> findByIdAndCategory(int id, Post.Category category);
}
