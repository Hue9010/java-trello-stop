package trello.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import trello.domain.User;
import trello.domain.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTest {
	private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

	@Resource(name = "userRepository")
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	public void create() {
		String email = "email@korea.kr";
		User newUser = new User("name", "password", email);
		userService.create(newUser);
		assertEquals(newUser, userRepository.findByEmail(email));
	}

}
