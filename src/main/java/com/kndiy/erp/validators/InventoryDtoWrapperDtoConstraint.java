package com.kndiy.erp.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = InventoryDtoWrapperDtoValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface InventoryDtoWrapperDtoConstraint {

    String message() default "Please input all Inventory Articles, or modify the Article Number by using corresponding button!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
