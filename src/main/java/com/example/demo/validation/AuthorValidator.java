package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<Author, String> {
    private static final String REGEX_AUTHOR_NAME = "[A-Za-z]+";

    @Override
    public boolean isValid(String inputAuthorName, ConstraintValidatorContext constraintValidatorContext) {
        return inputAuthorName.matches(REGEX_AUTHOR_NAME)
                && Character.isUpperCase(inputAuthorName.charAt(0))
                && inputAuthorName.length() < 30;
    }
}
