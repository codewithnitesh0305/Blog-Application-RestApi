package com.springboot.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Content is required")
    private String content;
    
    private String postImageName;
    
    @Transient
    private String url;

    @JsonIgnoreProperties("post")
    @ManyToOne
    private User user;

    @JsonIgnoreProperties("post")
    @ManyToOne
    private Category category;

    @JsonIgnoreProperties("post")
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

}
