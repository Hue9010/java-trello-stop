package trello.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import trello.domain.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserAcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);
	@Resource(name = "userRepository")
	private UserRepository userRepository;
	@Autowired
	private TestRestTemplate template;

	@Test
	public void createForm() {
		ResponseEntity<String> response = template.getForEntity("/users/create", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	private ResponseEntity<String> createPostResponse(MultiValueMap<String, Object> params, String path) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params,
				headers);

		return template.postForEntity(path, request, String.class);
	}

	private MultiValueMap<String, Object> createParams(String email, String name, String password) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("email", email);
		params.add("name", name);
		params.add("password", password);
		return params;
	}

	@Test
	public void create() throws Exception {
		MultiValueMap<String, Object> params = createParams("testUser@korea.kr", "testUser", "password");
		ResponseEntity<String> response = createPostResponse(params, "/users/create");

		assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
		assertTrue(userRepository.findByEmail("testUser@korea.kr").isPresent());
		assertThat(response.getHeaders().getLocation().getPath(), is("/"));
	}

	@Test
	public void loginForm() {
		ResponseEntity<String> response = template.getForEntity("/users/login", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	private MultiValueMap<String, Object> loginParams(String email, String password) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("email", email);
		params.add("password", password);
		return params;
	}

	@Test
	public void loginSuccess() {
		MultiValueMap<String, Object> params = loginParams("testUser@korea.kr", "password");
		ResponseEntity<String> response = createPostResponse(params, "/users/login");

		assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
		assertTrue(response.getHeaders().getLocation().getPath().contains("/;jsessionid="));
	}

	@Test
	public void loginEmptyEmail() {
		MultiValueMap<String, Object> params = loginParams("empty" + "testUser@korea.kr", "password");
		ResponseEntity<String> response = createPostResponse(params, "/users/login");

		assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
	}

	@Test
	public void loginOtherPassword() {
		MultiValueMap<String, Object> params = loginParams("testUser@korea.kr", "password" + "222");
		ResponseEntity<String> response = createPostResponse(params, "/users/login");

		assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
	}

}
