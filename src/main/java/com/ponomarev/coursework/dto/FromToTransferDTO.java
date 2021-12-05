package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class FromToTransferDTO {

	private String toFirstName;

	private String toLastName;

	private String toCardNumber;

	private String fromCardNumber;

	@NotEmpty(message = "Can not be empty")
	private String sum;

}
