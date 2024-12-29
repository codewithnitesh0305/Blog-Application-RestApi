package com.springboot.DTO;

import java.util.List;

import com.springboot.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {
    private int id;
    private String title;
    private String content;
    private String postImageName;
    private String url; 
    private UserDto user; 
    private CategoryDto category; 
    private List<CommentDto> comments;

    public PostDto(int id, String title, String content, String postImageName, String url, UserDto user, CategoryDto category, List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postImageName = postImageName;
        this.url = url;
        this.user = user;
        this.category = category;
        this.comments = comments;
    }

    public PostDto(int id, String title, String content, String postImageName, String url, UserDto user, CategoryDto category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postImageName = postImageName;
        this.url = url;
        this.user = user;
        this.category = category;
    }
}
