package com.ponomarev.coursework.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Template extends BaseEntity {

	private String firstName;

	private String lastName;

	private String cardNumber;

	@ManyToOne
	private User user;

}
