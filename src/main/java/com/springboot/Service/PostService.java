package com.springboot.Service;

import java.util.List;

import com.springboot.DTO.PostDto;
import com.springboot.Model.Post;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

	public PostDto createPost(Post post) throws Exception;
	public PostDto getPost(int id) throws Exception;
	public List<PostDto> getAllPost();
	public PostDto updatePost(Post post) throws Exception;
	public boolean deletePost(int id);
	public List<Post> searchPostByCategory(String categoryName);
	
}
