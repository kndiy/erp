package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemSellPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCodeRepository extends JpaRepository<ItemCode, Integer> {
    @Query("SELECT ic FROM ItemCode ic WHERE ic.itemCodeString = ?1")
    ItemCode findByItemCodeString(String itemCodeString);

    @Query("SELECT ic " +
            "FROM ItemCode ic " +
            "ORDER BY ic.itemType.itemCategory.itemCategoryString, ic.itemType.itemTypeString, ic.itemCodeString")
    List<ItemCode> findAllItemCodes();

    @Query("SELECT ic " +
            "FROM ItemCode ic " +
            "WHERE ic.itemType.itemCategory.idItemCategory = ?1 " +
            "ORDER BY ic.itemType.itemCategory.itemCategoryString, ic.itemType.itemTypeString, ic.itemCodeString")
    List<ItemCode> findByIdItemCategory(Integer idItemCategory);

    @Query("SELECT ic " +
            "FROM ItemCode ic " +
            "WHERE ic.itemType.idItemType = ?1 " +
            "ORDER BY ic.itemType.itemCategory.itemCategoryString, ic.itemType.itemTypeString, ic.itemCodeString")
    List<ItemCode> findByIdItemType(Integer idItemType);

    @Query("SELECT ic " +
            "FROM ItemCode ic " +
            "WHERE ?1 MEMBER OF ic.itemSellPriceList")
    List<ItemCode> findByItemSellPrice(ItemSellPrice itemSellPrice);
}
