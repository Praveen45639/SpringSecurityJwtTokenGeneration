package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.service.UserService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public BCryptPasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/user/welcome","/user/register","/user/authenticate")
				.permitAll()
				.anyRequest()
				.authenticated()
			    .and()
			     .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
				return http.build();	
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);;
		authProvider.setPasswordEncoder(pwdEncoder());
		return authProvider;
	}
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
   
}
