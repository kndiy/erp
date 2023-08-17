package com.kndiy.erp.validators;

import com.kndiy.erp.others.Quantity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class NumberStringValidator implements ConstraintValidator<NumberStringConstraint, String> {

    @Override
    public void initialize(NumberStringConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        try {
            new BigDecimal(s.replace(",",""));
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
