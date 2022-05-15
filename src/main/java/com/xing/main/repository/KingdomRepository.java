package com.xing.main.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.xing.main.model.Kingdom;

public interface KingdomRepository extends CrudRepository<Kingdom, Integer> {

	Kingdom findByName(String name);

	List<Kingdom> findAll();
}