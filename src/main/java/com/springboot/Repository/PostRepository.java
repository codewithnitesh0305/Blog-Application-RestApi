package com.springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.Model.Category;
import com.springboot.Model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

	public List<Post> findByUserId(int id);
    public List<Post> findByCategory(Category category);
	public List<Post> findByTitle(String name);
    

}
