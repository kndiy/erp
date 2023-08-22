package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.companyCluster.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query("SELECT c FROM Contact c WHERE c.address.idAddress = ?1")
    List<Contact> findAllByAddressID(Integer idAddress);

    @Query("SELECT c FROM Contact c WHERE c.contactName = ?1 AND c.address.idAddress = ?2")
    Contact findByContactNameAndAddress(String contactName, Integer idAddress);
    @Query("SELECT c FROM Contact c WHERE c.contactName = ?1")
    Contact findByContactName(String contactName);
    @Query("SELECT c FROM Contact c " +
            "WHERE c.address.company.companyType LIKE CONCAT('%', ?1, '%') " +
            "   AND c.address.addressType LIKE CONCAT('%', ?2, '%')")
    List<Contact> findByCompanyTypeAndAddressType(String companyType, String addressType);

    @Query("SELECT c FROM Contact c WHERE c.contactName = ?1 AND c.address.addressName = ?2")
    Contact findByContactNameAndAddressName(String orderPlacerContactName, String orderPlacerAddressName);

    @Query("SELECT c FROM Contact c " +
            "WHERE c.address.company.companyType LIKE CONCAT('%', ?1, '%')")
    List<Contact> findByCompanyType(String companyType);
}
