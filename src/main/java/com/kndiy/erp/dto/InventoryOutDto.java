package com.kndiy.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryOutDto implements Comparable<InventoryOutDto> {

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

    private String quantity;

    private String equivalent;

    private String equivalentUnit;

    private String inventoryOutPurpose;

    @Override
    public int compareTo(InventoryOutDto o) {
        return idInventoryOut.compareTo(o.idInventoryOut);
    }
}
