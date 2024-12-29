package com.springboot.Service;

import java.util.List;

import com.springboot.Model.Category;

public interface CategoryService {

	public Category createCategory(Category category);
	public Category getCategory(int id);
	public List<Category> getAllCategory();
	public Category updateCategory(Category category);
	public boolean deleteCategroy(int id);
}
