package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSavingAccountDTO {

	private String fromCardNumber;

	@NotNull(message = "Input value")
	private Double sum;

}
