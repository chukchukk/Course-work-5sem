package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TransactionDTO {

	private String firstName;

	private String lastName;

	private String cardNumber;

	private Map<String, String> cards;

}
