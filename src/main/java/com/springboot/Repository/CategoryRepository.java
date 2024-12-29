package com.springboot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.Model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

	// Method to find category by name
    public Category findByName(String name);
}
