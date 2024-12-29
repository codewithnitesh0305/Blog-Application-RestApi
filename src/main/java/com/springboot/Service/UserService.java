package com.springboot.Service;


import java.util.List;


import com.springboot.DTO.UserDto;
import com.springboot.Model.User;

public interface UserService {

	public User createUser(User user);
	public User getUser(int id);
	public List<UserDto> getAllUser();
	public User updateUser(User user);
	public boolean deleteUser(int id);
	
}
