package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.TreeSet;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    @Query("SELECT i.unit FROM Inventory i")
    TreeSet<String> findAllUnits();

    @Query("SELECT i FROM Inventory i WHERE i.inventoryIn.idInventoryIn = ?1")
    List<Inventory> findByIdInventoryIn(Integer idInventoryIn);


    @Query("SELECT i " +
            "FROM Inventory i " +
            "WHERE i.itemCode = ?1 " +
            "   AND i.inventoryIn.supplierSource = ?2 " +
            "   AND i.remainingQuantity > 0" +
            "ORDER BY i.storedAtAddress.idAddress, i.placementInWarehouse, i.productionCode")
    List<Inventory> findRemainingByItemCodeAndSupplier(ItemCode itemCode, Company supplier);

    @Query("SELECT i FROM Inventory i " +
            "WHERE i.productionCode = ?1 " +
            "   AND i.numberInBatch = ?2")
    Inventory findByProductionCodeAndNumberInBatch(String productionCode, Integer numberInBatch);
}
