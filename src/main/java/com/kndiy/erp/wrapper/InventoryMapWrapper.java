package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.inventoryCluster.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryMapWrapper {

    private TreeSet<Inventory> inventorySet;

}
