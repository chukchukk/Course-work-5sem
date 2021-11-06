package com.ponomarev.coursework.annotations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@NotNull
@NotEmpty
@NotBlank
public @interface StringValidation {
    String message() default "Incorrect field";
}
