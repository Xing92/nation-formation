package com.xing.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.xing.main.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsername(String username);
}