package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SavingAccountOperationDTO {

	private String cardNumber;

	@NotNull(message = "Input value")
	private Double sum;

}
