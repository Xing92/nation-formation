package com.xing.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyAuthenticationProvider authProvider;  
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
		http.csrf().disable().authorizeRequests().antMatchers("/user/create").permitAll().antMatchers("/user/all").permitAll().anyRequest().authenticated()
				.and().httpBasic();
		http.authenticationProvider(authProvider);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("USER");
		
		//TODO: add multiple users. Ale jak dodać usera, który został stworzony w Runtime ?
	}
}