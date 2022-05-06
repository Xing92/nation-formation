package com.xing.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ControllerTestBase {

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate restTemplate;

	String getUrl(String suffix) {
		return "http://localhost:" + port + suffix;
	}
	
	HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

}
