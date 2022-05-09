package com.xing.main;

import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.xing.main.model.User;
import com.xing.main.repository.UserRepository;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Authentication authenticate(final Authentication authentication) {
		final UsernamePasswordAuthenticationToken upAuth = (UsernamePasswordAuthenticationToken) authentication;
		final String name = (String) authentication.getPrincipal();

		final String password = (String) upAuth.getCredentials();

		System.out.println("name:" + name);
		System.out.println("pass:" + password);
		System.out.println("all users:");
		userRepository.findAll().forEach(u -> System.out.println("[" + u.getUsername() + ":" + u.getPassword() + "]"));

		final String storedPassword = userRepository.findByUsername(name).getPassword();

		if (Objects.equals(password, "") || !Objects.equals(password, storedPassword)) {
			throw new BadCredentialsException("illegal id or passowrd");
		}

		final Object principal = authentication.getPrincipal();
		final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
				authentication.getCredentials(), Collections.emptyList());
		result.setDetails(authentication.getDetails());

		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}
}