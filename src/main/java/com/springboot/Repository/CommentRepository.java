package com.springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.Model.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

	public List<Comment> findByPostId(int id);
}
