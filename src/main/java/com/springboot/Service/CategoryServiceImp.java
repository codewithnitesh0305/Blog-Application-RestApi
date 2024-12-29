package com.springboot.Service;

import java.util.List;

import com.springboot.Exception.CustomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.springboot.Model.Category;
import com.springboot.Model.User;
import com.springboot.Repository.CategoryRepository;
import com.springboot.Repository.UserRepository;

@Service
public class CategoryServiceImp implements CategoryService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category createCategory(Category category) {
		Category saveCategory = categoryRepository.save(category);
		if(ObjectUtils.isEmpty(saveCategory)){
			throw new CustomeException("Failure", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to save category",CategoryServiceImp.class);
		}
		return saveCategory;
	}

	@Override
	public Category getCategory(int id) {
		return categoryRepository.findById(id).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"There is not category available",CategoryServiceImp.class));
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> category = categoryRepository.findAll();
		if (category.isEmpty()){
			throw new CustomeException("Failure",HttpStatus.NOT_FOUND.value(), "There is no category found",CategoryServiceImp.class);
		}
		return category;
	}

	@Override
	public Category updateCategory(Category category) {
		Category updateCategory = categoryRepository.save(category);
		if(ObjectUtils.isEmpty(updateCategory)){
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to update category",CategoryServiceImp.class);
		}
		return updateCategory;
	}

	@Override
	public boolean deleteCategroy(int id) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(id).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(),"Category with Id: "+ id +"not found",CategoryServiceImp.class));
		if(!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		return false;
	}
	
}
