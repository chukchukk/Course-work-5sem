package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class FromToTransferDTO {

	private String toFirstName;

	private String toLastName;

	private String toCardNumber;

	private String fromCardNumber;

	@Min(value = 0, message = "Must more then 0")
	private Double sum;

}
