package com.example.demo.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FirstNameAndLastNameValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstNameAndLastName {
    String message() default "Invalid firstName or LastName";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
