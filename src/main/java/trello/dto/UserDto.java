package trello.dto;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import trello.domain.User;

@Setter
@Getter
@ToString
public class UserDto {
	@Size(min = 3, max = 20)
	private String name;
	@Size(min = 6, max = 20)
	private String password;
	@Size(min = 6, max = 50)
	private String email;

	public UserDto() {
	}

	public UserDto(String email, String password) {
		this("none", password, email);
	}

	public UserDto(String name, String password, String email) {
		this.name = name;
		this.password = password;
		this.email = email;
	}

	public User toUser() {
		return new User(name, password, email);
	}

}
