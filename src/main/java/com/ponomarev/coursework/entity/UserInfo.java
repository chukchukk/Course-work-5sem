package com.ponomarev.coursework.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_info_id")
    private PassportInfo passportInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_info_id")
    private Set<CardInfo> cardInfo;

    @OneToMany
    @JoinColumn(name = "user_info_id")
    private List<History> history;

}
