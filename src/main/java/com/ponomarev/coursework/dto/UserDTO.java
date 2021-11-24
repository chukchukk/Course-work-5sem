package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDTO {

	@NotBlank(message = "Type login correctly")
	@Size(min = 5, message = "Length of login should be >= 5 characters")
	private String login;

	@NotBlank(message = "Type password correctly")
	@Size(min = 8, message = "Length of password should be >= 8 characters")
	private String password;

}
