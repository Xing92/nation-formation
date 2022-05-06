package com.xing.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {

	@GetMapping("/sample")
	public String getSample() {
		System.out.println("Done");
		return "sample";
	}

}