package com.xing.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.xing.main.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Role findByRole(String role);
}
