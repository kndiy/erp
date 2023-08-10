package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SaleContainerRepository extends JpaRepository<SaleContainer, Integer> {

    SaleContainer findFirstBySaleArticle(SaleArticle art);

    SaleContainer findByContainerAndSaleArticle(String container, SaleArticle saleArticle);
    @Query("SELECT sc.orderUnit FROM SaleContainer sc")
    Set<String> findAllOrderUnit();

    @Query("SELECT c FROM SaleContainer c WHERE c.container = ?1 AND c.saleArticle.itemCode.itemCodeString = ?2 AND c.saleArticle.sale.orderName = ?3")
    SaleContainer findByContainerAndItemCodeStringAndOrderName(String container, String itemCodeString, String orderName);
}
