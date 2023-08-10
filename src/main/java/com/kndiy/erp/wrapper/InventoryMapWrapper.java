package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.inventoryCluster.Inventory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class InventoryMapWrapper {

    List<Inventory> inventoryList;

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

}
