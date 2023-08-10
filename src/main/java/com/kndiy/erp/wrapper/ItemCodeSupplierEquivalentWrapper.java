package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplierEquivalent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ItemCodeSupplierEquivalentWrapper {

    List<ItemCodeSupplierEquivalent> itemCodeSupplierEquivalentList;

    public List<ItemCodeSupplierEquivalent> getItemCodeSupplierEquivalentList() {
        return itemCodeSupplierEquivalentList;
    }

    public void setItemCodeSupplierEquivalentList(List<ItemCodeSupplierEquivalent> itemCodeSupplierEquivalentList) {
        this.itemCodeSupplierEquivalentList = itemCodeSupplierEquivalentList;
    }
}
