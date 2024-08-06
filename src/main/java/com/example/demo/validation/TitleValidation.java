package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidation implements ConstraintValidator<Title, String> {
    private static final String REGEX_TITLE = "^[a-zA-Z0-9]+$";

    @Override
    public boolean isValid(String inputTitle, ConstraintValidatorContext constraintValidatorContext) {
        return inputTitle.matches(REGEX_TITLE) && inputTitle.length() < 50;
    }
}
