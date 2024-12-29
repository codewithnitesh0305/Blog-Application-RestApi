package com.springboot.Service;

import com.springboot.DTO.CategoryDto;
import com.springboot.DTO.CommentDto;
import com.springboot.DTO.PostDto;
import com.springboot.DTO.UserDto;
import com.springboot.Exception.CustomeException;
import com.springboot.File.FileUpload;
import com.springboot.Model.Category;
import com.springboot.Model.Post;
import com.springboot.Repository.CategoryRepository;
import com.springboot.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class BlogDashboardServiceImp implements BlogDashboardService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileUpload fileUpload;

    @Override
    public List<PostDto> allPost() {
        List<Post> post = postRepository.findAll();
        if (post.isEmpty()){
            throw new CustomeException("Failure", HttpStatus.NOT_FOUND.value(),"There no post available",BlogDashboardServiceImp.class);
        }
        return post.stream().map(postItem -> {
                    try {
                        return convertToPostDto(postItem);
                    } catch (Exception e) {
                        throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error converting post to DTO: " + postItem.getId(),BlogDashboardServiceImp.class);
                    }
                }).toList();
    }

    @Override
    public PostDto postById(int id) throws Exception {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"Post with Id: "+ id + " Not Found",BlogDashboardServiceImp.class));
        return convertToPostDto(post);
    }

    @Override
    public List<PostDto> searchPost(String postName) {
        List<Post> post = postRepository.findByTitle(postName);
        if (post.isEmpty()){
            throw new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"There is not post available",BlogDashboardServiceImp.class);
        }
        return post.stream().map(postItem -> {
                    try {
                        return convertToPostDto(postItem);
                    } catch (Exception e) {
                        throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error converting post to DTO: " + postItem.getId(),BlogDashboardServiceImp.class);
                    }
                }).toList();
    }

    @Override
    public List<PostDto> postByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        List<Post> post = postRepository.findByCategory(category);
        if (ObjectUtils.isEmpty(category) || post.isEmpty()){
            throw new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"There is not post available",CategoryServiceImp.class);
        }
        return post.stream()
                .map(postItem -> {
                    try {
                        return convertToPostDto(postItem);
                    } catch (Exception e) {
                        throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error converting post to DTO: " + postItem.getId(),CategoryServiceImp.class);
                    }
                }).toList();
    }

    private PostDto convertToPostDto(Post post) throws Exception {
        String postImageName = fileUpload.getImageUrlByName(post.getPostImageName(), "blogDashboard");
        String profileImageName = fileUpload.getImageUrlByName(post.getUser().getProfileImageName(), "blogDashboard");

        // Check if the comment list is null and handle it gracefully
        List<CommentDto> commentDto = post.getComment() != null
                ? post.getComment().stream()
                .map(comment -> {
                    try {
                        return new CommentDto(
                                comment.getId(),
                                comment.getComment(),
                                new UserDto(
                                        comment.getUser().getId(),
                                        comment.getUser().getName(),
                                        comment.getUser().getEmail(),
                                        fileUpload.getImageUrlByName(comment.getUser().getProfileImageName(),"blogDashboard")
                                )
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList()
                : List.of(); // Return an empty list if comments are null

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPostImageName(),
                postImageName,
                new UserDto(
                        post.getUser().getId(),
                        post.getUser().getName(),
                        post.getUser().getEmail(),
                        profileImageName
                ),
                new CategoryDto(
                        post.getCategory().getId(),
                        post.getCategory().getName()
                ),
                commentDto // Include comments only if available
        );
    }
}
