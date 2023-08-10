package com.kndiy.erp.validators;

import com.kndiy.erp.dto.ItemSellPriceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ItemSellPriceDtoValidator implements ConstraintValidator<ItemSellPriceDtoConstraint, ItemSellPriceDto> {

    @Override
    public void initialize(ItemSellPriceDtoConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ItemSellPriceDto itemSellPriceDto, ConstraintValidatorContext constraintValidatorContext) {
        String itemSellPriceContract = itemSellPriceDto.getItemSellPriceContract().replace(",", " ").trim();

        try {
            Integer idItemSellPrice = Integer.parseInt(itemSellPriceContract);
            return true;
        }
        catch (Exception ex) {
            if (itemSellPriceDto.getFromDate() != null &&
                itemSellPriceDto.getToDate() != null &&
                itemSellPriceDto.getItemSellPriceAmount() != null &&
                itemSellPriceDto.getItemSellPriceUnit() != null &&
                itemSellPriceDto.getIdCustomer() != null) {
                return true;
            }
        }
        return false;
    }
}
