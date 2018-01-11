package trello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trello.domain.User;
import trello.domain.UserRepository;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;
	
	public void create(User user) {
		userRepository.save(user);
	}

	
}
