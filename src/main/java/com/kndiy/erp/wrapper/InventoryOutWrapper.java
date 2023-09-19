package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryOutWrapper {

    List<InventoryOut> inventoryOutList;

}
