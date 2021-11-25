package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangeLoginEmailDTO {

	@Size(min = 5, message = "Length of login should be >= 5 characters")
	private String login;

	@Email(message = "Incorrect email address")
	private String email;
}
