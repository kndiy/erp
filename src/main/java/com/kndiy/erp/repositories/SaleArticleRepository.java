package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleArticleRepository extends JpaRepository<SaleArticle, Integer> {
    @Query("SELECT sa FROM SaleArticle sa WHERE sa.sale.idSale = ?1 AND sa.itemCode = ?2")
    SaleArticle findByIdSaleAndItemCode(Integer idSale, ItemCode itemCode);

    SaleArticle findByIdSaleArticle(Integer idSaleArticle);

    @Query("SELECT s FROM SaleArticle s WHERE s.sale.orderName = ?1 AND s.itemCode.itemCodeString = ?2")
    SaleArticle findByOrderNameAndItemCodeString(String orderName, String itemCodeString);
}
