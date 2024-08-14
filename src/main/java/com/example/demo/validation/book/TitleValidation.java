package com.example.demo.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidation implements ConstraintValidator<Title, String> {
    private static final String REGEX_TITLE = "^[a-zA-Z0-9]+$";
    private static final int REGEX_VALID_TITLE_LENGTH = 50;

    @Override
    public boolean isValid(String inputTitle, ConstraintValidatorContext constraintValidatorContext) {
        return inputTitle.matches(REGEX_TITLE)
                && inputTitle.length() < REGEX_VALID_TITLE_LENGTH;
    }
}
