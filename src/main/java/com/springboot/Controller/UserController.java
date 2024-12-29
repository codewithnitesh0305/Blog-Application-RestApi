package com.springboot.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.DTO.CommentDto;
import com.springboot.DTO.PostDto;
import com.springboot.File.FileUpload;
import com.springboot.Model.Comment;
import com.springboot.Model.Post;
import com.springboot.Payload.SuccessResponse;
import com.springboot.Service.CommentService;
import com.springboot.Service.PostService;
import com.springboot.Service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FileUpload fileUpload;

    @PostMapping("/savePost")
    public ResponseEntity<?> createPost(@RequestParam("post") String posts, @RequestParam("file")MultipartFile file) throws Exception {
        String fileName = fileUpload.uploadFile(file);
        ObjectMapper objectMapper = new ObjectMapper();
        if(fileName != null){
            Post post = objectMapper.readValue(posts, Post.class);
            post.setPostImageName(fileName);
            PostDto postDto = postService.createPost(post);
            SuccessResponse<String> response = new SuccessResponse<>("Success", "Post save successfully!", HttpStatus.CREATED);
            return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.CREATED);
        }else {
            SuccessResponse<String> response = new SuccessResponse<>("Failure", "Post is required", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/AllPost")
    public ResponseEntity<?> getAllPost(){
        List<PostDto> postDto = postService.getAllPost();
        SuccessResponse<List<PostDto>> response = new SuccessResponse<>("Success", "Post retrieve successfully",postDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<List<PostDto>>>(response, HttpStatus.OK);
    }

    @GetMapping("/Post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") int id) throws Exception {
        PostDto postDto = postService.getPost(id);
        SuccessResponse<PostDto> response = new SuccessResponse<>("Failure", "Post retrieve successfully",postDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<PostDto>>(response, HttpStatus.OK);
    }

    @PutMapping("/UpdatePost/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") int id, @RequestParam("posts") String posts, @RequestParam("file") MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String fileName = file.getOriginalFilename();
        if(file.isEmpty()){
            throw new RuntimeException("File is Required");
        }
        Post post = objectMapper.readValue(posts,Post.class);
        post.setId(id);
        PostDto postDto = postService.updatePost(post);
        SuccessResponse<String> response = new SuccessResponse<>("Success", "Post updated successfully", HttpStatus.CREATED);
        return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/DeletePost/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") int id){
        postService.deletePost(id);
        SuccessResponse<String> response = new SuccessResponse<>("Success", "Post delete successfully", HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<String>>(response, HttpStatus.OK);
    }

    @PostMapping("/Comments/{id}")
    public ResponseEntity<?> createComments(@RequestBody Comment comment, @PathVariable("id") int postId) throws Exception {
        CommentDto commentDto = commentService.createComment(comment, postId);
        SuccessResponse<CommentDto> response = new SuccessResponse<>("Success", "Comment create successfully",commentDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<CommentDto>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComments(@RequestBody Comment comment, @PathVariable("id") int commentId) throws Exception {
         CommentDto commentDto = commentService.updateComment(comment, commentId);
        SuccessResponse<CommentDto> response = new SuccessResponse<>("Success", "Comment update successfully",commentDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<CommentDto>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments(){
        List<CommentDto> commentDto = commentService.getAllComment();
        SuccessResponse<List<CommentDto>> response = new SuccessResponse<>("Success", "All comments retrieve successfully",commentDto, HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<List<CommentDto>>>(response, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComments(@PathVariable("id") int commentId){
        commentService.deleteComment(commentId);
        SuccessResponse<CommentDto> response = new SuccessResponse<>("Success", "Comment delete successfully", HttpStatus.OK);
        return new ResponseEntity<SuccessResponse<CommentDto>>(response, HttpStatus.OK);
    }
}
