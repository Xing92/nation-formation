package com.xing.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.model.Role;
import com.xing.main.model.User;
import com.xing.main.repository.UserRepository;

@Controller
@RequestMapping(path = "/api/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/create")
	public @ResponseBody ResponseEntity<User> addNewUser(@RequestParam String username, @RequestParam String password) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		Role role = new Role();
		role.setRole("USER");
		user.addRole(role);
		user = userRepository.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping(path = "/me")
	public ResponseEntity<User> getMyUser() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(userName);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}