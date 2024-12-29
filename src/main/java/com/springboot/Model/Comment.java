package com.springboot.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String comment;

    // Ignore serialization of user inside Comment to avoid looping.
    @JsonIgnoreProperties({"comments", "post"})
    @ManyToOne
    private User user;

    // Ignore serialization of post inside Comment to avoid looping.
    @JsonIgnoreProperties({"comment"})
    @ManyToOne
    private Post post;
}
