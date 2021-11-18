package com.ponomarev.coursework.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CardInfoDTO {

    @NotBlank(message = "Type your card number correctly")
    @Size(min = 19, max = 19, message = "Incorrect card number")
    private String cardNumber;

    @NotBlank(message = "Type your valid thru correctly")
    private String validTHRU;

    @NotBlank(message = "Type cvv correctly")
    @Size(min = 3, max = 3, message = "CVV must contain 3 characters")
    private String cvv;
}
