package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entityCompositeKeys.ItemCodeSupplierKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCodeSupplierRepository extends JpaRepository<ItemCodeSupplier, ItemCodeSupplierKey> {
    @Query("SELECT ics FROM ItemCodeSupplier ics WHERE ics.itemCode.idItemCode = ?1 AND ics.supplier.idCompany = ?2")
    ItemCodeSupplier findByIdItemCodeAndIdSupplier(Integer idItemCode, Integer idSupplier); //idSupplier == idCompany
    @Query("SELECT ics FROM ItemCodeSupplier ics WHERE ics.itemCodeSupplierString = ?1")
    ItemCodeSupplier findByItemCodeSupplierString(String itemCodeSupplierString);

    @Query("SELECT i FROM ItemCodeSupplier i " +
            "WHERE i.itemCode.itemCodeString = ?1 " +
            "   AND i.supplier.nameEn = ?2")
    ItemCodeSupplier findByItemCodeStringAndSupplierNameEn(String itemCodeString, String supplierNameEn);

}
