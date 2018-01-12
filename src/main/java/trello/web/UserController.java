package trello.web;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import trello.UnAuthenticationException;
import trello.domain.UserRepository;
import trello.dto.UserDto;
import trello.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@GetMapping("/create")
	public String createForm() {
		return "/signUp";
	}

	@PostMapping("/create")
	public String create(UserDto userDto) {
		userService.create(userDto.toUser());
		return "redirect:/";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "/login";
	}

//	@PostMapping("/login")
//	public String login(UserDto userDto, HttpSession session) {
//		session.setAttribute("loginedUser", userService.login(userDto.toUser()));
//		return "redirect:/";
//	}

	@ExceptionHandler(UnAuthenticationException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public void unAuthentication() {
		log.debug("UnAuthenticationException is happened!");
	}
}
