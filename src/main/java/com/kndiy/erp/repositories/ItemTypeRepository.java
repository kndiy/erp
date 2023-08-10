package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Integer> {
    @Query("SELECT it FROM ItemType it WHERE it.itemTypeString = ?1")
    ItemType findByItemTypeString(String itemTypeString);
    @Query("SELECT it FROM ItemType it WHERE it.itemCategory.idItemCategory = ?1")
    List<ItemType> findByIdItemCategory(Integer idItemCategory);

}
