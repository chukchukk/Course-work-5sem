package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordDTO {

	@NotBlank(message = "Type your old password correctly")
	@Size(min = 8, message = "Length of password should be > 8 characters")
	private String oldPassword;

	@NotBlank(message = "Type your new password correctly")
	@Size(min = 8, message = "Length of password should be > 8 characters")
	private String passwordFirstTry;

	@NotBlank(message = "Type your password correctly")
	private String passwordSecondTry;

}
