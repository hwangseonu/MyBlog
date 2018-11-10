package me.mocha.blog.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Post {

    public enum Category {
        IT("it"),
        SPRING("spring"),
        DAILY("daily");

        String stringValue;

        Category(String stringValue) {
            this.stringValue = stringValue;
        }

        public static List<Category> categories() {
            List<Category> list = new ArrayList<>();
            list.add(IT);
            list.add(SPRING);
            list.add(DAILY);
            return list;
        }

        @Override
        public String toString() {
            return this.stringValue;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Post(String title, String content, LocalDateTime createAt, LocalDateTime updateAt, Category category) {
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.category = category;
    }

}
