package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryOutRepository extends JpaRepository<InventoryOut, Integer> {

    @Query("SELECT i FROM InventoryOut i WHERE i.inventory.inventoryIn.idInventoryIn = ?1")
    List<InventoryOut> findAllByIdInventoryIn(Integer idInventoryIn);
    @Query("SELECT i FROM InventoryOut i WHERE i.inventory.idInventory = ?1")
    List<InventoryOut> findByIdInventory(Integer idInventory);
}
