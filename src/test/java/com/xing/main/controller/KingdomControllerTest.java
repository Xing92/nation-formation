package com.xing.main.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.xing.main.model.User;
import com.xing.main.util.Log;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class KingdomControllerTest extends ControllerTestBase {
	@Autowired
	private KingdomController kingdomController;

	@Test
	public void sanityTest() {
		assertThat(kingdomController).isNotNull();
	}

	@Test
	public void createOneUserTest() {
		String username = "myusername";
		String password = "mypassword";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", username);
		params.add("password", password);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params,
				getHeaders());

		ResponseEntity<String> response = restTemplate.postForEntity(getUrl("/user/create"), request, String.class);

		List<User> users = restTemplate.getForObject(getUrl("/user/all"), List.class);
		Log.info("Returned users:" + users);
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getUserame()).isEqualTo(username);
		assertThat(users.get(0).getUserame()).isEqualTo(username);
		assertThat(users.get(0).getPassword()).isEqualTo(password);
	}
}
