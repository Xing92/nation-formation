package com.xing.main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.xing.main.model.User;
import com.xing.main.model.UserPrincipal;
import com.xing.main.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		System.out.println("XING:finding:username:" + username + ":");
		User user = userRepository.findByUsername(username);
		System.out.println("XING:finding:id:" + user.getId() + ":");
		return new UserPrincipal(user);
	}
}
