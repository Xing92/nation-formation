package com.xing.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.model.User;
import com.xing.main.repository.UserRepository;

@Controller
@RequestMapping(path = "/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/create")
	public @ResponseBody String addNewUser(@RequestParam String username, @RequestParam String password) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user = userRepository.save(user);
		return "User Created:" + user.getUserame();
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
}