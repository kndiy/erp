package com.kndiy.erp.validators;

import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.inventory.InventoryService;
import com.kndiy.erp.wrapper.InventoryOutDtoWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class InventoryOutDtoWrapperValidator implements ConstraintValidator<InventoryOutDtoWrapperConstraint, InventoryOutDtoWrapper> {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void initialize(InventoryOutDtoWrapperConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(InventoryOutDtoWrapper inventoryOutDtoWrapper, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        List<InventoryOutDto> inventoryOutDtoList = inventoryOutDtoWrapper.getInventoryOutDtoList();

        for (int i = 0; i < inventoryOutDtoList.size(); i ++) {

            InventoryOutDto out = inventoryOutDtoList.get(i);

            Quantity equivalentQuantity = new Quantity(out.getEquivalentQuantity(), out.getEquivalentUnit());
            Quantity splitQuantity = new Quantity(out.getSplitQuantity(), out.getEquivalentUnit());

            if (equivalentQuantity.lessThan(splitQuantity)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "Please check InventoryOut No. " + (i + 1) + " as Inventory Remaining amount is not enough!"
                ).addConstraintViolation();
                return false;
            }
        }


        return true;
    }
}
