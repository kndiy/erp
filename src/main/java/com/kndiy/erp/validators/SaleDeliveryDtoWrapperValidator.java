package com.kndiy.erp.validators;

import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleDeliveryDtoWrapperValidator implements ConstraintValidator<SaleDeliveryDtoWrapperConstraint, SaleDeliveryDtoWrapper> {

    @Override
    public void initialize(SaleDeliveryDtoWrapperConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper, ConstraintValidatorContext constraintValidatorContext) {

        Map<List<String>, List<String>> checkValidMap = new HashMap<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toPrintDate = saleDeliveryDtoWrapper.getDeliveryDate();
        Integer toPrintTurn = saleDeliveryDtoWrapper.getDeliveryTurn();
        constraintValidatorContext.disableDefaultConstraintViolation();

        for (SaleDeliveryDto saleDeliveryDto : saleDeliveryDtoWrapper.getSaleDeliveryDtoList()) {

            if (!toPrintDate.equals(saleDeliveryDto.getDeliveryDate()) || !toPrintTurn.equals(saleDeliveryDto.getDeliveryTurn())) {
                continue;
            }

            String deliveryDate = dtf.format(saleDeliveryDto.getDeliveryDate());
            String deliveryTurn = saleDeliveryDto.getDeliveryTurn().toString();
            List<String> key = List.of(deliveryDate, deliveryTurn);

            String from = saleDeliveryDto.getDepartFrom();
            String to = saleDeliveryDto.getDeliverTo();
            List<String> value = List.of(from, to);

            List<String> valueInMap = checkValidMap.get(key);
            if (valueInMap == null) {
                checkValidMap.put(key, value);
            }
            else if (!valueInMap.equals(value)) {

                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Departing location and Destination cannot differ in the same Delivery Turn! Please check accordingly!"
                ).addConstraintViolation();

                return false;
            }

        }

        return true;
    }
}
