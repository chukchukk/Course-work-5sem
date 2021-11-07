package com.ponomarev.coursework.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;

@Entity
@Table(name = "user_info", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class UserInfo extends BaseEntity{

    @NotBlank(message = "Type your first name correctly")
    private String firstName;

    @NotBlank(message = "Type your last name correctly")
    private String lastName;

    @NotBlank(message = "Type your middle name correctly")
    private String middleName;

    @NotBlank(message = "Type your email correctly")
    @Email(message = "Incorrect email address")
    private String email;

    @NotBlank(message = "Type your birthday date correctly")
    private String birthdayDate;
}
