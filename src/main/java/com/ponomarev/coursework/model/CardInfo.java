package com.ponomarev.coursework.model;

import com.ponomarev.coursework.annotations.StringValidation;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "card_info")
public class CardInfo extends BaseEntity {

    @StringValidation(message = "Type your card number correctly")
    @Size(min = 19, max = 19, message = "Incorrect card number")
    private String cardNumber;

    @StringValidation(message = "Type your valid thru correctly")
    @Size(min = 5, max = 5, message = "Incorrect date")
    @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}")
    private String validTHRU;

    @StringValidation(message = "Type field correctly")
    private String nameLastName;

    @StringValidation(message = "Type cvv correctly")
    @Size(min = 3, max = 3, message = "CVV must contain 3 characters")
    private String cvv;

    private boolean active;
}
