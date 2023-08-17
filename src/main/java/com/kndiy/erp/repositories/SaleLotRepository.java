package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.salesCluster.SaleLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleLotRepository extends JpaRepository<SaleLot, Integer> {
    @Query("SELECT sl " +
            "FROM SaleLot sl " +
            "WHERE sl.saleContainer.idSaleContainer = ?1 " +
            "       AND sl.lotName = ?2")
    SaleLot findByIdSaleContainerAndLotName(Integer idSaleContainer, String lotName);
    @Query("SELECT sl " +
            "FROM SaleLot sl " +
            "WHERE sl.saleContainer.idSaleContainer = ?1 " +
            "ORDER BY sl.idSaleLot")
    List<SaleLot> findAllByIdSaleContainer(Integer idSaleContainer);
    @Query("SELECT sl " +
            "FROM SaleLot sl " +
            "WHERE sl.saleContainer.saleArticle.sale.idSale = ?1 " +
            "ORDER BY sl.idSaleLot")
    List<SaleLot> findAllByIdSale(Integer idSale);
    @Query("SELECT sl " +
            "FROM SaleLot sl " +
            "WHERE sl.saleContainer.saleArticle.idSaleArticle = ?1 " +
            "ORDER BY sl.idSaleLot")
    List<SaleLot> findAllByIdSaleArticle(Integer idSaleArticle);

    @Query("SELECT s " +
            "FROM SaleLot s " +
            "WHERE s.deliveryDate > ?1 AND s.deliveryDate < ?2")
    List<SaleLot> findAllByDeliveryDate(LocalDate fromDate, LocalDate toDate);
}
