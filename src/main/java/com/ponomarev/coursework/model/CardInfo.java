package com.ponomarev.coursework.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "card_info", uniqueConstraints = @UniqueConstraint(columnNames = {"cardNumber"}))
public class CardInfo extends BaseEntity {

    @NotBlank(message = "Type your card number correctly")
    @Size(min = 19, max = 19, message = "Incorrect card number")
    private String cardNumber;

    @NotBlank(message = "Type your valid thru correctly")
    @Column(name = "valid_thru")
    private String validTHRU;

    @NotBlank(message = "Type cvv correctly")
    @Size(min = 3, max = 3, message = "CVV must contain 3 characters")
    private String cvv;

    private boolean active;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @Column
    private Double balance;
}
