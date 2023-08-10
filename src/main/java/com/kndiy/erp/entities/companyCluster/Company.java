package com.kndiy.erp.entities.companyCluster;

import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable {
    static private Integer order = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_company")
    private Integer idCompany;
    @NotBlank(message = "Company English name cannot be blank!")
    @Column(name = "company_name_en", unique = true)
    private String nameEn;
    @NotBlank(message = "Company Vietnamese name cannot be blank!")
    @Column(name = "company_name_vn")
    private String nameVn;
    @Column(name = "company_types")
    private String companyType;
    @Column(name = "company_industries")
    private String companyIndustry;
    @NotBlank(message = "Please enter Company abbreviation for ease of identification!")
    @Column(name = "company_abbreviation")
    private String abbreviation;
    @ManyToMany(mappedBy = "supplierList")
    private SortedSet<ItemCode> itemCodeList;
    public static enum CompanyTypeEnum {
        SELF, SUPPLIER, CUSTOMER, PARTNER, BANK
    }
    public static enum CompanyIndustryEnum {
        FABRIC, GARMENT, YARN, DYEING, KNITTING, MECHANICAL, TRADING, BANK, BEVERAGE, FOOD, PHARMACY
    }

    @Override
    public String toString(){
        return idCompany.toString();
    }

}
