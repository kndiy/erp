package com.kndiy.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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
}
