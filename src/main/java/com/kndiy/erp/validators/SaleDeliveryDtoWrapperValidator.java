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

        constraintValidatorContext.disableDefaultConstraintViolation();

        if (saleDeliveryDtoWrapper.getDeliveryDate() == null || saleDeliveryDtoWrapper.getDeliveryTurn() == null) {

            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Please input Delivery Date and Turn by selecting corresponding Summary Grid!"
            ).addConstraintViolation();
            return false;
        }

        Map<List<String>, List<String>> checkValidMap = new HashMap<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate toPrintDate = saleDeliveryDtoWrapper.getDeliveryDate();
        Integer toPrintTurn = saleDeliveryDtoWrapper.getDeliveryTurn();
        String toPrintSaleSource = saleDeliveryDtoWrapper.getSaleSource();

        for (SaleDeliveryDto saleDeliveryDto : saleDeliveryDtoWrapper.getSaleDeliveryDtoList()) {

            if (!toPrintDate.equals(saleDeliveryDto.getDeliveryDate()) ||
                    !toPrintTurn.equals(saleDeliveryDto.getDeliveryTurn()) ||
                    !toPrintSaleSource.equals(saleDeliveryDto.getSaleSource())) {

                continue;
            }

            String saleSource = saleDeliveryDto.getSaleSource();
            String customer = saleDeliveryDto.getCustomer();
            String deliveryDate = dtf.format(saleDeliveryDto.getDeliveryDate());
            String deliveryTurn = saleDeliveryDto.getDeliveryTurn().toString();
            List<String> key = List.of(saleSource, customer, deliveryDate, deliveryTurn);

            String from = saleDeliveryDto.getDepartFrom();
            String to = saleDeliveryDto.getDeliverTo();
            List<String> value = List.of(from, to);

            List<String> valueInMap = checkValidMap.get(key);
            if (valueInMap == null) {
                checkValidMap.put(key, value);
            }
            else if (!valueInMap.equals(value)) {

                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "SaleSource, Customer, Departing location and Destination cannot differ in the same Delivery Turn! Please check and modify turn numbers accordingly!"
                ).addConstraintViolation();

                return false;
            }

        }

        return true;
    }
}
