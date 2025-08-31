package com.example.demo.service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	BCryptPasswordEncoder pwdEncoder;
	
	public User saveUser(User user) {
		String encoder = pwdEncoder.encode(user.getPassword());
		user.setPassword(encoder);
		return userRepo.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
   
	public Optional<User> getById(int id) {
		return userRepo.findById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepo.findByUsername(username);
	    if (user == null) {
	        throw new UsernameNotFoundException("User not found: " + username);
	    }
	    return new org.springframework.security.core.userdetails.User(
	            user.getUsername(),
	            user.getPassword(),
	            Collections.singleton(() -> user.getUser_roles().toUpperCase()) // ✅ Role mapping
	    );
	}

}
