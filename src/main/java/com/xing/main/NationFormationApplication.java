package com.xing.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xing.main.model.Year;
import com.xing.main.repository.YearRepository;

@SpringBootApplication
public class NationFormationApplication {
	
	@Autowired
	static YearRepository yearRepository;

	public static void main(String[] args) {
		SpringApplication.run(NationFormationApplication.class, args);
		System.out.println("Xing:started");
	}
	

}
