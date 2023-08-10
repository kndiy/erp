package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class InventoryOutDto {

    private Integer idInventoryOut;
    private Integer idInventory;
    private String storedAtAddress;
    private String placementInWarehouse;
    private String productionCode;
    private String remainingQuantity;
    private String inventoryUnit;
    private Boolean takeAll;
    private Boolean split;
    private String splitQuantity;
    private String equivalentValue;
    private String equivalentQuantity;
    private String equivalentUnit;
    private String inventoryOutPurpose;

    public String getInventoryOutPurpose() {
        return inventoryOutPurpose;
    }

    public void setInventoryOutPurpose(String inventoryOutPurpose) {
        this.inventoryOutPurpose = inventoryOutPurpose;
    }

    public String getStoredAtAddress() {
        return storedAtAddress;
    }

    public void setStoredAtAddress(String storedAtAddress) {
        this.storedAtAddress = storedAtAddress;
    }

    public String getPlacementInWarehouse() {
        return placementInWarehouse;
    }

    public void setPlacementInWarehouse(String placementInWarehouse) {
        this.placementInWarehouse = placementInWarehouse;
    }

    public String getProductionCode() {
        return productionCode;
    }

    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public Boolean getTakeAll() {
        return takeAll;
    }

    public void setTakeAll(Boolean takeAll) {
        this.takeAll = takeAll;
    }



    public String getEquivalentUnit() {
        return equivalentUnit;
    }

    public void setEquivalentUnit(String equivalentUnit) {
        this.equivalentUnit = equivalentUnit;
    }

    public Integer getIdInventoryOut() {
        return idInventoryOut;
    }

    public void setIdInventoryOut(Integer idInventoryOut) {
        this.idInventoryOut = idInventoryOut;
    }


    public Integer getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(Integer idInventory) {
        this.idInventory = idInventory;
    }

    public String getInventoryUnit() {
        return inventoryUnit;
    }

    public void setInventoryUnit(String inventoryUnit) {
        this.inventoryUnit = inventoryUnit;
    }

    public String getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Boolean getSplit() {
        return split;
    }

    public void setSplit(Boolean split) {
        this.split = split;
    }

    public String getSplitQuantity() {
        return splitQuantity;
    }

    public void setSplitQuantity(String splitQuantity) {
        this.splitQuantity = splitQuantity;
    }

    public String getEquivalentValue() {
        return equivalentValue;
    }

    public void setEquivalentValue(String equivalentValue) {
        this.equivalentValue = equivalentValue;
    }

    public String getEquivalentQuantity() {
        return equivalentQuantity;
    }

    public void setEquivalentQuantity(String equivalentQuantity) {
        this.equivalentQuantity = equivalentQuantity;
    }
}
