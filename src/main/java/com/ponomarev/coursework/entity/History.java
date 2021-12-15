package com.ponomarev.coursework.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "history")
@Entity
@Getter
@Setter
public class History extends BaseEntity{

	private String message;

	@Temporal(TemporalType.DATE)
	private Date date;

	@ElementCollection(targetClass = History.OperationType.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Set<OperationType> operationType;

	public enum OperationType {
		TRANSFER, SAVING_ACCOUNT_OPERATION, CARD_OPERATION
	}

	@ManyToOne
	private UserInfo userInfo;

}
