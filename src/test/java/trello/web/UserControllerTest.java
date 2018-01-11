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
public class UserControllerTest {
	private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
	@Resource(name="userRepository")
	private UserRepository userRepository;
	@Autowired
	private TestRestTemplate template;

	@Test
	public void createFormTest() {
		ResponseEntity<String> response = template.getForEntity("/users/create", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void loginFormTest() {
		ResponseEntity<String> response = template.getForEntity("/users/login", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void create() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String email = "testUser@korea.kr";
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("email", email);
		params.add("name", "testUser");
		params.add("password", "password");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params,
				headers);

		ResponseEntity<String> response = template.postForEntity("/users/create", request, String.class);

		assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
		assertTrue(userRepository.findByEmail(email).isPresent());
		assertThat(response.getHeaders().getLocation().getPath(), is("/"));
	}
}
