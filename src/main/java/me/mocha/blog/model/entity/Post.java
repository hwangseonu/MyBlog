package me.mocha.blog.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Builder
    public Post(String title, String content, LocalDateTime createAt, LocalDateTime updateAt) {
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

}
