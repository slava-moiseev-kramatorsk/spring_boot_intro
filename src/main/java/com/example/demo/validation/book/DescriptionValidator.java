package com.example.demo.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DescriptionValidator implements ConstraintValidator<Description, String> {
    private static final String REGEX_DESCRIPTION = "^[a-zA-Z0-9\\s]+$";
    private static final int VALID_DESCRIPTION_LENGTH = 1000;

    @Override
    public boolean isValid(
            String inputDescription,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return (inputDescription.length() < VALID_DESCRIPTION_LENGTH
                && inputDescription.matches(REGEX_DESCRIPTION))
                || (inputDescription.isEmpty());
    }
}
