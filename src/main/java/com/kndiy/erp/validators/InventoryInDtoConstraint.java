package com.kndiy.erp.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = InventoryInDtoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InventoryInDtoConstraint {

    String message() default "Please either enter VND Value or a full set of Foreign Value - Unit - Exchange Rate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
