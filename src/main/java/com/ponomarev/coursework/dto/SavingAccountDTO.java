package com.ponomarev.coursework.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SavingAccountDTO {

	private Double balance;

	private Double minBalance;

	private Double accrual;

}
