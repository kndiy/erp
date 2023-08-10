package com.kndiy.erp.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ItemSellPriceDtoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemSellPriceDtoConstraint {
    String message() default "Please choose a Contract or input a new complete Contract";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
