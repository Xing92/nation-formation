package com.xing.main.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.xing.main.model.Role;
import com.xing.main.model.User;
import com.xing.main.util.Log;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends ControllerTestBase {

	@Autowired
	private UserController userController;

	@Test
	public void sanityTest() {
		assertThat(userController).isNotNull();
	}

	@Test
	public void createOneUserViaControllerTest() {
		String username = "myusername";
		String password = "mypassword";

		userController.addNewUser(username, password);
		Iterable<User> usersIterable = userController.getAllUsers();
		List<User> users = StreamSupport.stream(usersIterable.spliterator(), false).collect(Collectors.toList());
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getUsername()).isEqualTo(username);
		assertThat(users.get(0).getPassword()).isEqualTo(password);
		assertThat(users.get(0).getRoles()).hasSize(1);
		assertThat(users.get(0).getRoles()).contains(new Role("USER"));
	}

	@Test
	public void createOneUserViaRestTest() {
		String username = "myusername";
		String password = "mypassword";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", username);
		params.add("password", password);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params,
				getHeaders());

		ResponseEntity<String> response = restTemplate.postForEntity(getUrl("/api/user/create"), request, String.class);

		String response2 = sendRequest(getUrl("/api/user/all"));
		System.out.println("XING:response:");
		System.out.println(response2);
		
//		List<User> users = restTemplate.getForObject(getUrl("/api/user/all"), List.class);
//		Log.info("Returned users:" + users);
//		assertThat(users).hasSize(1);
//		assertThat(users.get(0).getUsername()).isEqualTo(username);
//		assertThat(users.get(0).getPassword()).isEqualTo(password);
//		assertThat(users.get(0).getRoles()).hasSize(1);
//		assertThat(users.get(0).getRoles()).contains(new Role("USER"));
	}
	
	private String sendRequest(String urlAddress) {
		
		String username = "myusername";
		String password = "mypassword";
		String method = "GET";
		
		String response = "noResponse";
		try {
			URL url = new URL(urlAddress);
			String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = in.readLine()) != null) {
				response = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
