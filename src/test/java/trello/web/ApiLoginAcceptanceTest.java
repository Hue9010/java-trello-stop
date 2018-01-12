package trello.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import lombok.extern.java.Log;
import trello.dto.UserDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log
public class ApiLoginAcceptanceTest {
	@Autowired
	private TestRestTemplate template;

	@Value("${local.server.port}")
	private int serverPort;

	@Before
	public void setup() {
		RestAssured.port = serverPort;
	}

	@Test
	public void loginSuccess() throws Exception {
		UserDto newUser = new UserDto("hue@korea.kr", "password");
		given()
			.contentType(ContentType.JSON)
			.body(newUser)
		.when().post("/api/login")
		.then().statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void loginEmptyEmail() throws Exception {
		UserDto newUser = new UserDto("wwww" + "hue@korea.kr", "password");
		given()
			.contentType(ContentType.JSON)
			.body(newUser)
			.when().post("/api/login")
			.then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	public void loginOtherPassword() throws Exception {
		UserDto newUser = new UserDto("hue@korea.kr", "password" + "222");
		given().contentType(ContentType.JSON).body(newUser)
			.when().post("/api/login")
				.then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
}
