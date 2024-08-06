package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnBookValidator implements ConstraintValidator<Isbn, String> {
    private static final String REGEX_ISBN_BOOK = "[0-9-]+";

    @Override
    public boolean isValid(String inputIsbn, ConstraintValidatorContext constraintValidatorContext) {
        return inputIsbn.matches(REGEX_ISBN_BOOK)
                && inputIsbn.length() < 100;
    }
}
