package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Integer> {
    @Query("SELECT ic FROM ItemCategory ic WHERE ic.itemCategoryString = ?1")
    ItemCategory findByItemCategoryString(String itemCategoryString);
}
