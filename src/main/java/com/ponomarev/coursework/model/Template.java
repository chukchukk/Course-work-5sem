package com.ponomarev.coursework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Template extends BaseEntity {

	private String firstName;

	private String lastName;

	private String cardNumber;

	@ManyToOne
	private User user;

}
