package com.springboot.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.Exception.CustomeException;
import com.springboot.File.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.springboot.DTO.CategoryDto;
import com.springboot.DTO.CommentDto;
import com.springboot.DTO.PostDto;
import com.springboot.DTO.UserDto;
import com.springboot.Model.Category;
import com.springboot.Model.Post;
import com.springboot.Model.User;
import com.springboot.Repository.CategoryRepository;
import com.springboot.Repository.PostRepository;
import com.springboot.Repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServiceImp implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileUpload fileUpload;
	
	@Override
	public PostDto createPost(Post post) throws Exception {
	    User user = getAuthority();
	    String categoryName = post.getCategory() != null ? post.getCategory().getName() : null;
	    if (ObjectUtils.isEmpty(categoryName)) {
	        throw new RuntimeException("Category name is required");
	    }
	    Category category = categoryRepository.findByName(categoryName);
	    if (ObjectUtils.isEmpty(category)) {
	        throw new CustomeException("Failure", HttpStatus.NOT_FOUND.value(), "Category: " + categoryName+" not found",PostServiceImp.class);
	    }

	    post.setCategory(category);
	    post.setUser(user);
	    Post savedPost = postRepository.save(post);
	    return convertToPostDto(savedPost);
	}


	@Override
	public PostDto getPost(int id) throws Exception {
		// TODO Auto-generated method stub
		User user = getAuthority();
		if(ObjectUtils.isEmpty(user)) {
			throw new RuntimeException("Invalid User");
		}
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post with Id: "+ id + " not found"));
		if(!post.getUser().getEmail().equals(user.getEmail())) {
			throw new RuntimeException("Unauthorized access to this post");
		}
		return convertToPostDto(post);
	}

	@Override
	public List<PostDto> getAllPost() {
	    // Get the authenticated user
	    User user = getAuthority();
	    if (ObjectUtils.isEmpty(user)) {
	        throw new RuntimeException("Invalid User");
	    }
		List<Post> post = postRepository.findByUserId(user.getId());
		if(post.isEmpty()){
			throw new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"There is not post available",PostServiceImp.class);
		}
		return post.stream()
				.map(postItem -> {
					try {
						return convertToPostDto(postItem);
					} catch (Exception e) {
						throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error converting post to DTO: " + postItem.getId(),PostServiceImp.class);
					}
				})
				.toList();
	}


	@Override
	public PostDto updatePost(Post post) throws Exception {
		// TODO Auto-generated method stub
		User user = getAuthority();
		post.setUser(user);
		Post savePost = postRepository.save(post);
		if(ObjectUtils.isEmpty(savePost)){
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to update post",PostServiceImp.class);
		}
		return  convertToPostDto(post);
	}

	@Override
	public boolean deletePost(int id) {
		User user = getAuthority();
		Post post = postRepository.findById(id).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(), "Post with Id: "+ id + " not found",PostServiceImp.class));
		if(!post.getUser().getEmail().equals(user.getEmail())) {
			throw new RuntimeException("Invalid User");
		}
		 postRepository.delete(post);
		return true;
	}
	
	public User  getAuthority() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email);
		if(ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("Invalid User with email: "+ email);
		}
		return user;
	}

	@Override
	public List<Post> searchPostByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        if (category != null) {
            return postRepository.findByCategory(category);
        }
        return null;
	}


	private PostDto convertToPostDto(Post post) throws Exception {
		String postImageName = fileUpload.getImageUrlByName(post.getPostImageName(), "user");
		String profileImageName = fileUpload.getImageUrlByName(post.getUser().getProfileImageName(), "user");

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
										fileUpload.getImageUrlByName(comment.getUser().getProfileImageName(),"user")
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
