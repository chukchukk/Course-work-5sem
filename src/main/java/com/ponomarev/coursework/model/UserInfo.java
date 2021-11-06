package com.ponomarev.coursework.model;

import com.ponomarev.coursework.annotations.StringValidation;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity{

    @StringValidation(message = "Type your first name correctly")
    private String firstName;

    @StringValidation(message = "Type your last name correctly")
    private String lastName;

    @StringValidation(message = "Type your middle name correctly")
    private String middleName;

    @StringValidation(message = "Type your passport series correctly")
    @Size(min = 4, max = 4, message = "Length of passport's series must be 4 characters")
    private String passportSeries;

    @StringValidation(message = "Type your passport number correctly")
    @Size(min = 6, max = 6, message = "Length of passport's number must be 6 characters")
    private String passportNumber;

    @StringValidation(message = "Type your phone number correctly")
    @Size(min = 11, max = 12, message = "The phone number must start with +7 or 8 and don't contain spaces.")
    private String phoneNumber;

    @StringValidation(message = "Type your email correctly")
    @Email(message = "Incorrect email address")
    private String email;

    @StringValidation(message = "Type your country correctly")
    private String country;

    @StringValidation(message = "Type your city correctly")
    private String city;

    @StringValidation(message = "Type your address correctly")
    private String address;
}
