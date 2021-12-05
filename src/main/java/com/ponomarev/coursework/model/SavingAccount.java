package com.ponomarev.coursework.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "saving_account")
@Getter
@Setter
public class SavingAccount extends BaseEntity{

	private String createdDate;

	private String updatedDate;

	private Double balance;

}
