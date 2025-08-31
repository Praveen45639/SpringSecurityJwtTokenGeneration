package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	AuthenticationManager authManager;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome this endpoint this is not secure";
	}
	
	@PostMapping("/register")
	public User saveUser(@RequestBody User user) {
		return service.saveUser(user);
	}
    
	@GetMapping("/all-users")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> getAllUsers(){
		return service.getAllUsers();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Optional<User> getById(@PathVariable int id) {
		return service.getById(id);
	}
	
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody User user) {
	Authentication authenticate =authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	if(authenticate.isAuthenticated()) {
		return jwtService.generateToken(user.getUsername());
	  }
	else {
	throw new RuntimeException("invalid username and password");
	    }
	
	 }
}