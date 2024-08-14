package com.example.demo.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<Author, String> {
    private static final String REGEX_AUTHOR_NAME = "[A-Za-z]+";
    private static final int VALID_LENGTH_NAME = 30;
    private static final int INDEX_OF_FIRST_LETTER = 0;

    @Override
    public boolean isValid(
            String inputAuthorName,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        return inputAuthorName.matches(REGEX_AUTHOR_NAME)
                && Character.isUpperCase(inputAuthorName.charAt(INDEX_OF_FIRST_LETTER))
                && inputAuthorName.length() < VALID_LENGTH_NAME;
    }
}
