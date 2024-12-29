package com.springboot.Controller;

import java.util.List;

import com.springboot.DTO.UserDto;
import com.springboot.File.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.Model.Category;
import com.springboot.Model.User;
import com.springboot.Payload.SuccessResponse;
import com.springboot.Service.CategoryService;
import com.springboot.Service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin API's", description = "Only admin can access this endpoint")
public class AdminController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private FileUpload fileUpload;
	
										/* Category and User are Managed By Admin */
	
													/* Category EndPoint */
	@PostMapping("/Category")
	public ResponseEntity<?> createCategory(@RequestBody Category category){
		Category createCategory = categoryService.createCategory(category);
		if(createCategory != null) {
	        SuccessResponse<Category> response = new SuccessResponse<>("Success","Category Created Successfully",HttpStatus.CREATED);
	        return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.CREATED);
		}else {
	        SuccessResponse<Category> response = new SuccessResponse<>("Success","Category not created",HttpStatus.INTERNAL_SERVER_ERROR);
	        return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.CREATED);
		}

	}
	
	@GetMapping("/Category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable("id") int id){
		Category category = categoryService.getCategory(id);
		if(category != null) {
	        SuccessResponse<Category> response = new SuccessResponse<>("Success","Category Reterived Successfully",category,HttpStatus.OK);
	        return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.OK);
		}else {
	        SuccessResponse<Category> response = new SuccessResponse<>("Success","Category Not Found",HttpStatus.NOT_FOUND);
	        return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.OK);
		}

	}
	
	@GetMapping("/Category")
	public ResponseEntity<?> getAllCategory(){
		List<Category> allCategory = categoryService.getAllCategory();
		if(!allCategory.isEmpty()) {
	        SuccessResponse<List<Category>> response = new SuccessResponse<>("Success","Reterived All Category Successfully",allCategory,HttpStatus.OK);
	        return new ResponseEntity<SuccessResponse<List<Category>>>(response,HttpStatus.OK);
		}else {
	        SuccessResponse<List<Category>> response = new SuccessResponse<>("Success","No data available",allCategory,HttpStatus.NOT_FOUND);
	        return new ResponseEntity<SuccessResponse<List<Category>>>(response,HttpStatus.OK);
		}

	}
	
	@PutMapping("/Category/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable("id") int id, @RequestBody Category category){
		category.setId(id);
		Category updateCategory = categoryService.updateCategory(category);
		if(updateCategory != null) {
			SuccessResponse<Category> response = new SuccessResponse<>("Success","Category Update Successfully",HttpStatus.CREATED);
		    return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.CREATED);
		}else {
			SuccessResponse<Category> response = new SuccessResponse<>("Failuer","Category Not Updated",HttpStatus.INTERNAL_SERVER_ERROR);
		    return new ResponseEntity<SuccessResponse<Category>>(response,HttpStatus.CREATED);
		}

	}
	
	@DeleteMapping("/Category/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") int id){
		boolean deleteCategory = categoryService.deleteCategroy(id);
		if(deleteCategory) {
			SuccessResponse<Boolean> response = new SuccessResponse<>("Success","Category Update Successfully",HttpStatus.OK);
			return new ResponseEntity<SuccessResponse<Boolean>>(response,HttpStatus.CREATED);
		}else {
			SuccessResponse<Boolean> response = new SuccessResponse<>("Failure","Category Not Deleted",HttpStatus.INTERNAL_SERVER_ERROR);
			return new ResponseEntity<SuccessResponse<Boolean>>(response,HttpStatus.CREATED);
		}

	}
	
												/* Users EndPoint */
	
	@GetMapping("/Users")
	public ResponseEntity<?> getAllUsers() throws Exception {
		List<UserDto> allUser = userService.getAllUser();
		if(!allUser.isEmpty()) {
			for(UserDto dto : allUser){
				String userProfileImage = fileUpload.getImageUrlByName(dto.getProfileImage(), "admin");
				dto.setProfileImage(userProfileImage);
			}
			SuccessResponse<List<UserDto>> response = new SuccessResponse<>("Success","Retrieved All Users Successfully",allUser, HttpStatus.OK);
	        return new ResponseEntity<SuccessResponse<List<UserDto>>>(response,HttpStatus.OK);
		}else {
			SuccessResponse<List<UserDto>> response = new SuccessResponse<>("Success","No data available",allUser, HttpStatus.INTERNAL_SERVER_ERROR);
	        return new ResponseEntity<SuccessResponse<List<UserDto>>>(response,HttpStatus.OK);
		}

	}
	
	@GetMapping("/Users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") int id){
		User user = userService.getUser(id);
		if(user != null) {
			SuccessResponse<User> response = new SuccessResponse<>("Success","Users Retrieved Successfully",user, HttpStatus.OK);
	        return new ResponseEntity<SuccessResponse<User>>(response,HttpStatus.OK);
		}else {
			SuccessResponse<User> response = new SuccessResponse<>("Success","Data is not available", HttpStatus.NOT_FOUND);
	        return new ResponseEntity<SuccessResponse<User>>(response,HttpStatus.OK);
		}

	}
}
