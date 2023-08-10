package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOutWrapper {
    List<InventoryOut> inventoryOutList;

    public List<InventoryOut> getInventoryOutList() {
        return inventoryOutList;
    }

    public void setInventoryOutList(List<InventoryOut> inventoryOutList) {
        this.inventoryOutList = inventoryOutList;
    }
}
