package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import java.util.Objects;

public class FieldMatchValidation implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object inputValue, ConstraintValidatorContext constraintValidatorContext) {
        Object field = new BeanWrapperImpl(inputValue).getPropertyValue(this.firstFieldName);
        Object fieldMatch = new BeanWrapperImpl(inputValue).getPropertyValue(this.secondFieldName);
        return Objects.equals(field, fieldMatch);
    }
}
