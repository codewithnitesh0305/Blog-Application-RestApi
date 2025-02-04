package com.springboot.Config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.springboot.Model.User;
import com.springboot.Repository.UserRepository;

@Component
public class CustomeUserDetails implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("User with email: "+ email + " not found");
        }
        return new CustomeUser(user);
	}

	

}
