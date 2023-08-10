package com.kndiy.erp.validators;

import com.kndiy.erp.dto.InventoryInDto;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.inventory.InventoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryInDtoValidator implements ConstraintValidator<InventoryInDtoConstraint, InventoryInDto> {

    @Autowired
    private InventoryService inventoryService;
    @Override
    public void initialize(InventoryInDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(InventoryInDto inventoryInDto, ConstraintValidatorContext constraintValidatorContext) {
        //disable default message
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (inventoryInDto.getIdInventoryIn() != null && !inventoryInDto.getNumberOfInventoryArticles().equals("")) {
            try {
                int oldArticleNumber = inventoryService.findByIdInventoryIn(inventoryInDto.getIdInventoryIn()).size();
                int newArticleNumber = Integer.parseInt(inventoryInDto.getNumberOfInventoryArticles());

                if (newArticleNumber < oldArticleNumber) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "New Article number is less than existing Articles!"
                    ).addConstraintViolation();
                    return false;
                }
            } catch (NumberFormatException ex) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Article number is not a valid number!"
                ).addConstraintViolation();
                return false;
            }
        }

        if (!inventoryInDto.getInventoryInValue().equals("") &&
                !inventoryInDto.getInventoryInValueForeign().equals("") &&
                !inventoryInDto.getForeignUnit().equals("") &&
                !inventoryInDto.getExchangeRate().equals("")) {

            return subIsValidAllPopulate(inventoryInDto, constraintValidatorContext);
        }
        else if (!inventoryInDto.getInventoryInValue().equals("")) {

            return subIsValidOnlyValue(inventoryInDto, constraintValidatorContext);
        }
        else if (!inventoryInDto.getInventoryInValueForeign().equals("") &&
                !inventoryInDto.getForeignUnit().equals("") &&
                !inventoryInDto.getExchangeRate().equals("")) {

            return subIsValidForeign(inventoryInDto, constraintValidatorContext);
        }

        constraintValidatorContext.buildConstraintViolationWithTemplate(
                "You have to enter either Local value or a full set of Foreign value!"
        ).addConstraintViolation();
        return false;
    }

    private boolean subIsValidOnlyValue(InventoryInDto inventoryInDto, ConstraintValidatorContext constraintValidatorContext) {
        try {
            new Quantity(inventoryInDto.getInventoryInValue(), "VND");
            return true;
        }
        catch (NumberFormatException ex) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "InventoryIn value is not a valid Number!"
            ).addConstraintViolation();
            return false;
        }
    }

    private boolean subIsValidForeign(InventoryInDto inventoryInDto, ConstraintValidatorContext constraintValidatorContext) {
        try {
            new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND");
            return true;
        }
        catch (NumberFormatException ex) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "InventoryIn Foreign value and Exchange Rate is not a valid Number!"
            ).addConstraintViolation();
            return false;
        }
    }

    private boolean subIsValidAllPopulate(InventoryInDto inventoryInDto, ConstraintValidatorContext constraintValidatorContext) {
        try {
            new Quantity(inventoryInDto.getInventoryInValue(), "VND");
            new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND");
            return true;
        }
        catch (NumberFormatException ex) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Entering all value fields and got one wrong is not better than entering only one value that is right..."
            ).addConstraintViolation();
            return false;
        }
    }
}
