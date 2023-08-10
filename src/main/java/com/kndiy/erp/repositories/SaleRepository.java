package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.salesCluster.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT s FROM Sale s WHERE s.orderName = ?1")
    Sale findByOrderName(String orderName);
}
