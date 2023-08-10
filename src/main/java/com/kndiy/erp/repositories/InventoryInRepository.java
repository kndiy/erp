package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryInRepository extends JpaRepository<InventoryIn, Integer> {

    @Query("SELECT ii FROM InventoryIn ii ORDER BY ii.createdAt DESC")
    List<InventoryIn> findAllInventoriesIn();

    @Query("SELECT ii FROM InventoryIn ii WHERE ii.voucher = ?1")
    InventoryIn findByVoucher(String voucher);
}
