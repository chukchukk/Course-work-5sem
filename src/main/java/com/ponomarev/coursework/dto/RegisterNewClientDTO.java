package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegisterNewClientDTO {
    @NotBlank(message = "Type last name correctly")
    private String lastName;

    @NotBlank(message = "Type first name correctly")
    private String firstName;

    @NotBlank(message = "Type middle name correctly")
    private String middleName;

    @Email(message = "Incorrect email address")
    private String email;

    @NotBlank(message = "Type birthday date correctly")
    private String birthdayDate;

    @Size(min = 4, max = 4, message = "Length of passport's series must be 4 characters")
    private String passportSeries;

    @Size(min = 6, max = 6, message = "Length of passport's number must be 6 characters")
    private String passportNumber;

    @NotBlank(message = "Type passport issue date correctly")
    private String passportIssueDate;
}
