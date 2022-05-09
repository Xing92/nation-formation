package com.xing.main;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.xing.main.model.Year;
import com.xing.main.repository.YearRepository;

@Configuration
public class InitialConfiguration {

	@Autowired
	private YearRepository yearRepository;

	@PostConstruct
	public void init() {
//		createYear();
//		System.out.println("XING:year created");
	}

	private void createYear() {
		Year year = new Year();
		year.setYearNumer(1);
		yearRepository.save(year);
	}

}
