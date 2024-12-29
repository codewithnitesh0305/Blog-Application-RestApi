package com.springboot.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;
    
    private String profileImageName;
    
    @Transient
    private String url;

    // Ignore the post inside User to prevent circular references
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private List<Post> post;

    // Ignore comments inside User to prevent circular references
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @Enumerated(value = EnumType.STRING)
    private Roles roles;

    // Ignore refreshToken during serialization
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;
}
