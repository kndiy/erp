package com.kndiy.erp.validators;

import com.kndiy.erp.dto.InventoryDto;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.inventory.InventoryService;
import com.kndiy.erp.wrapper.InventoryDtoWrapperDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InventoryDtoWrapperDtoValidator implements ConstraintValidator<InventoryDtoWrapperDtoConstraint, InventoryDtoWrapperDto> {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void initialize(InventoryDtoWrapperDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(InventoryDtoWrapperDto inventoryDtoWrapperDto, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        boolean isFine = true;
        if (inventoryDtoWrapperDto.getNewArticleNumber().isEmpty()) {

            List<InventoryDto> inventoryDtoList = inventoryDtoWrapperDto.getInventoryDtoList();
            for (InventoryDto inventoryDto : inventoryDtoList) {
                try {
                    new Quantity(inventoryDto.getInitQuantity(), inventoryDto.getUnit());
                }
                catch (Exception ex) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "InitQuantity of one entry or more is not a valid number!"
                    ).addConstraintViolation();
                    isFine = false;
                    break;
                }
                if (inventoryDto.getUnit().charAt(0) == '0') {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Please select a Unit!"
                    ).addConstraintViolation();
                    isFine = false;
                    break;
                }
                if (inventoryDto.getSupplierProductionCode().isEmpty()) {
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Please input all ProductionCode!"
                    ).addConstraintViolation();
                    isFine = false;
                    break;
                }
            }
            return isFine;
        }

        try {
            int articleNum = Integer.parseInt(inventoryDtoWrapperDto.getNewArticleNumber());
            List<Inventory> inventoryList = inventoryService.findByIdInventoryIn(inventoryDtoWrapperDto.getIdInventoryIn());
            int oldArticleNum = inventoryList.size();
            if (articleNum < oldArticleNum) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "New ArticleNumber is smaller than current total Inventory count!"
                ).addConstraintViolation();
                return false;
            }

            return true;
        }
        catch (NumberFormatException ex) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "New Article number is not a valid number!"
            ).addConstraintViolation();
            return false;
        }
    }
}
