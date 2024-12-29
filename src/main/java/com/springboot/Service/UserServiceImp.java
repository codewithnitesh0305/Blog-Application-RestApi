package com.springboot.Service;

import java.util.List;

import com.springboot.DTO.UserDto;
import com.springboot.Exception.CustomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.Model.Roles;
import com.springboot.Model.User;
import com.springboot.Repository.UserRepository;
import org.springframework.util.ObjectUtils;

@Service
public class UserServiceImp implements UserService{

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User createUser(User user) {
		// TODO Auto-generated method stub
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRoles(Roles.ROLE_USER);
		return userRepository.save(user);
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with User Id: "+ id + " Not Found"));
		UserDto dto = convertToUserDTo(user);
		return user;
	}

	@Override
	public List<UserDto> getAllUser() {
		// TODO Auto-generated method stub
		List<User> users = userRepository.findAll();
		if (ObjectUtils.isEmpty(users)){
			throw new CustomeException("Failure", HttpStatus.NOT_FOUND.value(),"There is not user found",UserServiceImp.class);
		}
		return users.stream().map(this::convertToUserDTo).toList();
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		User updateUser = userRepository.save(user);
		if (ObjectUtils.isEmpty(updateUser)){
			throw new CustomeException("Failure",HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to update",UserServiceImp.class);
		}
		return userRepository.save(user);
	}

	@Override
	public boolean deleteUser(int id) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(id).orElseThrow(() -> new CustomeException("Failure",HttpStatus.NOT_FOUND.value(), "User With User Id: "+ id + " Not Found",UserServiceImp.class));
		userRepository.delete(user);
		return true;
	}
	public UserDto convertToUserDTo(User user){
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setProfileImage(user.getProfileImageName());
		return dto;
	}


}
