package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.companyCluster.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT a FROM Address a WHERE a.company.idCompany = ?1")
    List<Address> findByCompanyId(Integer idCompany);

    @Query("SELECT a FROM Address a WHERE a.idAddress = ?1")
    Address findByIdAddress(Integer id);

    @Query("SELECT a FROM Address a WHERE a.addressName = ?1")
    Address findByAddressName(String addressName);

    @Query("SELECT a " +
            "FROM Address a " +
            "WHERE a.company.companyType LIKE CONCAT('%', ?1, '%') " +
            "ORDER BY a.addressName ASC")
    List<Address> findByCompanyType(String companyType);

    @Query("SELECT a " +
            "FROM Address a " +
            "WHERE a.company.companyType LIKE CONCAT('%', ?1, '%') " +
            "OR a.company.companyType LIKE CONCAT('%', ?2, '%') " +
            "ORDER BY a.addressName ASC")
    List<Address> findByCompanyType(String companyType1, String companyType2);
}
