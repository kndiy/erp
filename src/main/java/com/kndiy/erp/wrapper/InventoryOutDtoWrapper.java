package com.kndiy.erp.wrapper;

import com.kndiy.erp.dto.InventoryOutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryOutDtoWrapper {

    private List<InventoryOutDto> inventoryOutDtoList;
    private Integer idSaleLot;
    private Integer idManufacture;
    private Integer idWarehouseTransfer;

    public InventoryOutDtoWrapper(List<InventoryOutDto> inventoryOutDtoList, Integer idSaleLot) {
    }
}
