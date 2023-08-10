package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplierEquivalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCodeSupplierEquivalentRepository extends JpaRepository<ItemCodeSupplierEquivalent, Integer> {

    @Query("SELECT DISTINCT e.equivalentUnit " +
            "FROM ItemCodeSupplierEquivalent e")
    List<String> findEquivalentUnits();

    @Query("SELECT DISTINCT e.sourceUnit " +
            "FROM ItemCodeSupplierEquivalent e")
    List<String> findSourceUnits();

    @Query("SELECT e FROM ItemCodeSupplierEquivalent e WHERE e.itemCodeSupplier = ?1")
    List<ItemCodeSupplierEquivalent> findByItemCodeSupplier(ItemCodeSupplier itemCodeSupplier);

    @Query("SELECT e FROM ItemCodeSupplierEquivalent e " +
            "WHERE e.itemCodeSupplier = ?1 " +
            "   AND e.sourceUnit = ?2 " +
            "   AND e.equivalentUnit = ?3")
    ItemCodeSupplierEquivalent findByItemCodeSupplierAndUnits(ItemCodeSupplier itemCodeSupplier, String sourceUnit, String equivalentUnit);
}
