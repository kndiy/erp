package com.kndiy.erp.wrapper;

import com.kndiy.erp.dto.InventoryOutDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class InventoryOutDtoWrapper {
    private List<InventoryOutDto> inventoryOutDtoList;

    private Integer idSaleLot;
    private Integer idManufacture;
    private Integer idWarehouseTransfer;

    public InventoryOutDtoWrapper(List<InventoryOutDto> inventoryOutDtoList, Integer idSaleLot) {
        this.inventoryOutDtoList = inventoryOutDtoList;
        this.idSaleLot = idSaleLot;
    }

    public Integer getIdManufacture() {
        return idManufacture;
    }

    public void setIdManufacture(Integer idManufacture) {
        this.idManufacture = idManufacture;
    }

    public Integer getIdWarehouseTransfer() {
        return idWarehouseTransfer;
    }

    public void setIdWarehouseTransfer(Integer idWarehouseTransfer) {
        this.idWarehouseTransfer = idWarehouseTransfer;
    }

    public Integer getIdSaleLot() {
        return idSaleLot;
    }

    public void setIdSaleLot(Integer idSaleLot) {
        this.idSaleLot = idSaleLot;
    }

    public List<InventoryOutDto> getInventoryOutDtoList() {
        return inventoryOutDtoList;
    }

    public void setInventoryOutDtoList(List<InventoryOutDto> inventoryOutDtoList) {
        this.inventoryOutDtoList = inventoryOutDtoList;
    }
}
