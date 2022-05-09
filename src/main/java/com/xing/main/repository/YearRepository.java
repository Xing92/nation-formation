package com.xing.main.repository;

import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.xing.main.model.Year;

public interface YearRepository extends CrudRepository<Year, Integer> {

	default public Year findLatestYear() {
		return StreamSupport.stream(findAll().spliterator(), false).sorted((o1, o2) -> {
			if (o1.getYearNumer() > o2.getYearNumer())
				return -1;
			if (o1.getYearNumer() < o2.getYearNumer())
				return 1;
			return 0;
		}).findFirst().get();
	}
}