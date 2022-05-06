package com.xing.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.xing.main.model.Year;

public interface YearRepository extends CrudRepository<Year, Integer> {
}