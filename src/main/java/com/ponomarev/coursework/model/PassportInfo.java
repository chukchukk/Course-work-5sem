package com.ponomarev.coursework.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class PassportInfo extends BaseEntity{

    @NotBlank(message = "Type your passport series correctly")
    @Size(min = 4, max = 4, message = "Length of passport's series must be 4 characters")
    private String passportSeries;

    @NotBlank(message = "Type your passport number correctly")
    @Size(min = 6, max = 6, message = "Length of passport's number must be 6 characters")
    private String passportNumber;

    private String issueDate;
}
