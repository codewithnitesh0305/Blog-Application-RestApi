package com.springboot.Service;

import java.util.List;

import com.springboot.DTO.CommentDto;
import com.springboot.Model.Comment;

public interface CommentService {

	public CommentDto createComment(Comment comment, int postId) throws Exception;
	public Comment getComment(int id);
	public List<CommentDto> getAllComment();
	public CommentDto updateComment(Comment comment,int postId) throws Exception;
	public void deleteComment(int id);
}
