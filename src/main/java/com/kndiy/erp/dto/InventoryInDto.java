package com.kndiy.erp.dto;

import com.kndiy.erp.validators.InventoryInDtoConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@InventoryInDtoConstraint
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

    public void setSupplierSource(String supplierSource) {
        this.supplierSource = supplierSource;
    }

    public String getSupplierSource() {
        return supplierSource;
    }

    public String getNumberOfInventoryArticles() {
        return numberOfInventoryArticles;
    }

    public void setNumberOfInventoryArticles(String numberOfInventoryArticles) {
        this.numberOfInventoryArticles = numberOfInventoryArticles;
    }

    public String getInventoryInSource() {
        return inventoryInSource;
    }

    public void setInventoryInSource(String inventorySource) {
        this.inventoryInSource = inventorySource;
    }


    public String getInventoryInValue() {
        return inventoryInValue;
    }

    public void setInventoryInValue(String inventoryInValue) {
        this.inventoryInValue = inventoryInValue;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getInventoryInValueForeign() {
        return inventoryInValueForeign;
    }

    public void setInventoryInValueForeign(String inventoryInValueForeign) {
        this.inventoryInValueForeign = inventoryInValueForeign;
    }

    public String getForeignUnit() {
        return foreignUnit;
    }

    public void setForeignUnit(String foreignUnit) {
        this.foreignUnit = foreignUnit;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Integer getIdInventoryIn() {
        return idInventoryIn;
    }

    public void setIdInventoryIn(Integer idInventoryIn) {
        this.idInventoryIn = idInventoryIn;
    }
}
