package com.springboot.Service;

import com.springboot.DTO.PostDto;

import java.util.List;

public interface BlogDashboardService {
    public List<PostDto> allPost() throws Exception;
    public PostDto postById(int id) throws Exception;
    public List<PostDto> searchPost(String postName);
    public List<PostDto> postByCategory(String categoryName);
}
