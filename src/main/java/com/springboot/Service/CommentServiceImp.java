package com.springboot.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

import com.springboot.Exception.CustomeException;
import com.springboot.File.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.springboot.DTO.CommentDto;
import com.springboot.DTO.UserDto;
import com.springboot.Model.Comment;
import com.springboot.Model.Post;
import com.springboot.Model.User;
import com.springboot.Repository.CommentRepository;
import com.springboot.Repository.PostRepository;
import com.springboot.Repository.UserRepository;

@Service
public class CommentServiceImp implements CommentService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	public PostRepository postRepository;

	@Autowired
	public FileUpload fileUpload;
	
	@Override
	public CommentDto createComment(Comment comment, int postId) throws Exception {
		// TODO Auto-generated method stub
		User user = getAuthority();
		comment.setUser(user);
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomeException("Failure", HttpStatus.NOT_FOUND.value(),"Post with Id: "+ postId+ " not found",CommentServiceImp.class));
		comment.setPost(post);
		Comment saveComment = commentRepository.save(comment);
		if(ObjectUtils.isEmpty(saveComment)){
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to save comment",CommentServiceImp.class);
		}
		return convertIntoCommentDto(saveComment);
	}

	@Override
	public Comment getComment(int id) {
		// TODO Auto-generated method stub
		User user = getAuthority();
		if(ObjectUtils.isEmpty(user)) {
			throw new RuntimeException("Invalid User");
		}
		Comment comment = commentRepository.findById(id).get();
		if(!comment.getUser().getEmail().equals(user.getEmail())) {
			throw new RuntimeException("Unauthorized access to this comment");
		}
		return comment;
	}

	@Override
	public List<CommentDto> getAllComment() {
		// TODO Auto-generated method stub
		List<Comment> commentList = commentRepository.findAll();
		if(commentList.isEmpty()){
			throw new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"No comments available",CommentServiceImp.class);
		}
		return commentList.stream().map(comment -> {
			try {
				return convertIntoCommentDto(comment);
			} catch (Exception e) {
				throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Error converting post to DTO: " + comment.getId(),BlogDashboardServiceImp.class);
			}
		}).collect(Collectors.toList());
	}

	@Override
	public CommentDto updateComment(Comment comment, int postId) throws Exception {
		User user = getAuthority();
		if(!comment.getUser().getEmail().equals(user.getEmail())) {
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid User with Email: "+ comment.getUser().getEmail(),CommentServiceImp.class);
		}
		comment.setUser(user);
		Post post = postRepository.findById(postId).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"Post with Id: "+ postId+" not found",CommentServiceImp.class) );
		comment.setPost(post);
		Comment createComment =  commentRepository.save(comment);
		return convertIntoCommentDto(createComment);
	}

	@Override
	public void deleteComment(int id) {
		// TODO Auto-generated method stub
		User user = getAuthority();
		Comment comment = commentRepository.findById(id).orElseThrow(()-> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"Comment with Id: "+ id + " not found",CommentServiceImp.class));
		if(!comment.getUser().getEmail().equals(user.getEmail())) {
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid User with Email: "+ comment.getUser().getEmail(),CommentServiceImp.class);
		}
		 commentRepository.delete(comment);
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
	
	public CommentDto convertIntoCommentDto(Comment comment) throws Exception {
		return new CommentDto(
				comment.getId(),
				comment.getComment(),
				new UserDto(
						comment.getUser().getId(),
						comment.getUser().getEmail(),
						comment.getUser().getName(),
						fileUpload.getImageUrlByName(comment.getUser().getProfileImageName(), "user")
				)
		);
	}
}
