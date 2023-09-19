package com.kndiy.erp.dto;

import com.kndiy.erp.validators.InventoryInDtoConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@InventoryInDtoConstraint
@Setter
@Getter
public class InventoryInDto {
    private Integer idInventoryIn;
    @Pattern(regexp = "^[^0-9]*$", message = "Please choose a Source for this InventoryIn")
    private String inventoryInSource;
    private String inventoryInValue; //cost, price in VND
    private String exchangeRate;
    private String inventoryInValueForeign;
    private String foreignUnit;
    @NotBlank(message = "Cannot validate Inventories without corresponding voucher!")
    private String voucher; //contracts, ect.
    private String numberOfInventoryArticles;
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Supplier for this InventoryIn")
    private String supplierSource;


}
