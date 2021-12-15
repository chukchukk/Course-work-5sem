package com.ponomarev.coursework.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "saving_account")
@Getter
@Setter
public class SavingAccount extends BaseEntity{

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	private Double balance;

	private Double minBalance;

	private Integer day;

	private boolean isActive;
}
