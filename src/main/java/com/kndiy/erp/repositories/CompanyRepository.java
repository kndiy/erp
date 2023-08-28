package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.companyCluster.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("SELECT c FROM Company c WHERE c.idCompany = ?1")
    Company findCompanyByCompanyId(Integer id);

    @Query("SELECT c FROM Company c WHERE c.nameEn = ?1")
    Company findCompanyByNameEn(String nameEn);
    @Query("SELECT c FROM Company c WHERE c.nameVn = ?1")
    Company findCompanyByNameVn(String nameVn);
    @Query("SELECT c FROM Company c WHERE c.companyType LIKE CONCAT('%', ?1, '%')")
    List<Company> findByCompanyType(String companyType);
    @Query("SELECT c FROM Company c WHERE c.companyType LIKE CONCAT('%', ?1, '%') OR c.companyType LIKE CONCAT('%', ?2, '%')")
    List<Company> findByCompanyType(String companyType1, String companyType2);

    @Query("SELECT c FROM Company c WHERE c.abbreviation = ?1")
    Company findByAbbreviation(String abbreviation);
}
