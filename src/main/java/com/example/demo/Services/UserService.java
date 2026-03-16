package com.example.demo.Services;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
private final UserRepository userrepo;
private final BCryptPasswordEncoder passwordEncode;

@Autowired
public UserService(UserRepository userrepo) {
	this.userrepo = userrepo;
	this.passwordEncode = new BCryptPasswordEncoder();
}
public User registerUser(User user) {
	if(userrepo.findByUsername(user.getUsername()).isPresent()) {
		throw new RuntimeException("Username is already taken.");
	}
	if(userrepo.findByEmail(user.getEmail()).isPresent()) {
		throw new RuntimeException("Email is already taken.");
	}
	user.setPassword(passwordEncode.encode(user.getPassword()));
	return userrepo.save(user);
}
}
