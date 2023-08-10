package com.kndiy.erp.validators;

import com.kndiy.erp.dto.SaleLotDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SaleLotDtoValidator implements ConstraintValidator<SaleLotDtoConstraint, SaleLotDto> {
    @Override
    public void initialize(SaleLotDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SaleLotDto saleLotDto, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (saleLotDto.getIdFromAddress().equals("0") && saleLotDto.getIdToAddress().equals("0")) {
            return true;
        }

        if (!saleLotDto.getIdFromAddress().equals("0") && saleLotDto.getIdToAddress().equals("0") ||
                saleLotDto.getIdFromAddress().equals("0") && !saleLotDto.getIdToAddress().equals("0")
        ) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Departure and Destination addresses have to be defined together!"
            ).addConstraintViolation();
            return false;
        }

        if (!saleLotDto.getIdToAddress().equals("0") && saleLotDto.getIdContactReceiver().equals("0")) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Receiver has to be selected after defining Destination so as drivers can contact them!"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
