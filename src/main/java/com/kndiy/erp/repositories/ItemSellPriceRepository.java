package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemSellPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSellPriceRepository extends JpaRepository<ItemSellPrice, Integer> {
    @Query("SELECT isp FROM ItemSellPrice isp WHERE isp.itemSellPriceContract = ?1 AND isp.itemSellPriceAmount = ?2 AND isp.itemSellPriceUnit = ?3 ORDER BY isp.fromDate DESC")
    ItemSellPrice findByContractAndAmountAndUnit(String contract, Float itemSellPriceAmount, String itemSellPriceUnit);
    @Query("SELECT isp FROM ItemSellPrice isp WHERE isp.itemSellPriceContract = ?1 ORDER BY isp.fromDate DESC")
    ItemSellPrice findByContract(String itemSellPriceContract);
    @Query("SELECT isp FROM ItemSellPrice isp ORDER BY isp.fromDate DESC")
    List<ItemSellPrice> findAll();
}
