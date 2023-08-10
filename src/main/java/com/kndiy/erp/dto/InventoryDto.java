package com.kndiy.erp.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto {
    private Integer idItemCode;
    private Integer idInventoryIn;
    private Integer idAddressStoredAt;
    private Integer numberInBatch;
    private String placementInWarehouse;
    private String supplierProductionCode;
    private String initQuantity;
    private String remainingQuantity;
    private String unit;
    private Integer idInventory;

    public Integer getNumberInBatch() {
        return numberInBatch;
    }

    public void setNumberInBatch(Integer numberInBatch) {
        this.numberInBatch = numberInBatch;
    }

    public Integer getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(Integer idInventory) {
        this.idInventory = idInventory;
    }

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }

    public Integer getIdInventoryIn() {
        return idInventoryIn;
    }

    public void setIdInventoryIn(Integer idInventoryIn) {
        this.idInventoryIn = idInventoryIn;
    }

    public Integer getIdAddressStoredAt() {
        return idAddressStoredAt;
    }

    public void setIdAddressStoredAt(Integer idAddressStoredAt) {
        this.idAddressStoredAt = idAddressStoredAt;
    }

    public String getPlacementInWarehouse() {
        return placementInWarehouse;
    }

    public void setPlacementInWarehouse(String placementInWarehouse) {
        this.placementInWarehouse = placementInWarehouse;
    }

    public String getSupplierProductionCode() {
        return supplierProductionCode;
    }

    public void setSupplierProductionCode(String supplierProductionCode) {
        this.supplierProductionCode = supplierProductionCode;
    }

    public String getInitQuantity() {
        return initQuantity;
    }

    public void setInitQuantity(String initQuantity) {
        this.initQuantity = initQuantity;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
