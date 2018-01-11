package trello.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trello.UnAuthenticationException;
import trello.domain.User;
import trello.domain.UserRepository;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;

	public void create(User user) {
		// TODO 아래와 같은 로직을 추가 할지 Restful API 형태로 변경 할 때 고민해 보자
		// Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		// if (optionalUser.isPresent()) {
		// throw new AlreadyExistedUserException();
		// }
		userRepository.save(user);
	}

	public User login(User user) {
		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		if (!optionalUser.isPresent()) {
			throw new UnAuthenticationException();
		}
		User dbUser = optionalUser.get();
		if (!dbUser.matchPassword(user.getPassword())) {
			throw new UnAuthenticationException();
		}
		return dbUser;
	}

}
