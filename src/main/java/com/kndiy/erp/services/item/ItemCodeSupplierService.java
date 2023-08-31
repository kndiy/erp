package com.kndiy.erp.services.item;

import com.kndiy.erp.dto.ItemCodeSupplierDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entityCompositeKeys.ItemCodeSupplierKey;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.repositories.ItemCodeSupplierRepository;
import com.kndiy.erp.services.CompanyClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemCodeSupplierService {
    @Autowired
    private ItemCodeSupplierRepository itemCodeSupplierRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private CompanyClusterService companyClusterService;
    public String addNewItemCodeSupplier(ItemCodeSupplierDto itemCodeSupplierDto) {
        ItemCode itemCode = itemCodeRepository.findById(itemCodeSupplierDto.getIdItemCode()).orElse(null);
        Company supplier = companyRepository.findById(itemCodeSupplierDto.getIdCompany()).orElse(null);

        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierRepository.findByItemCodeSupplierString(itemCodeSupplierDto.getItemCodeSupplierString());

        if (itemCodeSupplier != null) {
            return "Item Code Supplier named: " + itemCodeSupplier.getItemCodeSupplierString() + " already exists under Supplier: " + itemCodeSupplier.getSupplier().getNameEn()
                    + " and ItemCode: " + itemCodeSupplier.getItemCode().getItemCodeString();
        }

        ItemCodeSupplierKey itemCodeSupplierKey = new ItemCodeSupplierKey(supplier.getIdCompany(), itemCode.getIdItemCode());

        itemCodeSupplier = new ItemCodeSupplier();
        itemCodeSupplier.setIdItemCodeSupplier(itemCodeSupplierKey);
        itemCodeSupplier.setSupplier(supplier);
        itemCodeSupplier.setItemCode(itemCode);
        itemCodeSupplier.setItemCodeSupplierString(itemCodeSupplierDto.getItemCodeSupplierString());

        itemCodeSupplier = itemCodeSupplierRepository.save(itemCodeSupplier);

        return "Successfully created new Item Code Supplier named: " + itemCodeSupplier.getItemCodeSupplierString() + " of ItemCode named: " + itemCodeSupplier.getItemCode().getItemCodeString() + " from Supplier: " + itemCodeSupplier.getSupplier().getNameEn();
    }

    public List<String> getModifiedItemCodeSupplierErrors(Errors errors) {
        List<String> res = new ArrayList<>();
        errors.getFieldErrors().forEach(fieldError -> {
            res.add(fieldError.getDefaultMessage());
        });
        return res;
    }

    public String editItemCodeSupplier(ItemCodeSupplierDto itemCodeSupplierDto) {
        ItemCode itemCode = itemCodeRepository.findById(itemCodeSupplierDto.getIdItemCode()).orElse(null);
        Company supplier = companyRepository.findById(itemCodeSupplierDto.getNewIdCompany()).orElse(null);
        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierRepository.findByIdItemCodeAndIdSupplier(itemCodeSupplierDto.getIdItemCode(),itemCodeSupplierDto.getIdCompany());
        ItemCodeSupplier checkExistingNewSupplier = itemCodeSupplierRepository.findByIdItemCodeAndIdSupplier(itemCodeSupplierDto.getIdItemCode(), itemCodeSupplierDto.getNewIdCompany());

        if (checkExistingNewSupplier != null &&
                checkExistingNewSupplier.getItemCodeSupplierString().equals(itemCodeSupplier.getItemCodeSupplierString()) &&
                !itemCodeSupplierDto.getIdCompany().equals(itemCodeSupplierDto.getNewIdCompany())) {

            return "ItemCodeSupplier under ItemCode named: " + checkExistingNewSupplier.getItemCode().getItemCodeString() + " AND Supplier named: " + checkExistingNewSupplier.getSupplier().getNameEn() + " already exists!";
        }

        if (itemCodeSupplier == null) {
            return "ItemCodeSupplier named:" + itemCodeSupplierDto.getItemCodeSupplierString() + " does not exist anymore!";
        }

        itemCodeSupplierRepository.delete(itemCodeSupplier);

        ItemCodeSupplierKey itemCodeSupplierKey = new ItemCodeSupplierKey(itemCodeSupplierDto.getNewIdCompany(), itemCodeSupplierDto.getIdItemCode());
        itemCodeSupplier = new ItemCodeSupplier();

        itemCodeSupplier.setIdItemCodeSupplier(itemCodeSupplierKey);
        itemCodeSupplier.setSupplier(supplier);
        itemCodeSupplier.setItemCode(itemCode);
        itemCodeSupplier.setItemCodeSupplierString(itemCodeSupplierDto.getItemCodeSupplierString());
        itemCodeSupplierRepository.save(itemCodeSupplier);

        return "Successfully edited Item Code Supplier of Item Code: " + itemCodeSupplier.getItemCodeSupplierString();
    }

    public String deleteItemCodeSupplier(Integer idItemCode, Integer idSupplier) {
        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierRepository.findByIdItemCodeAndIdSupplier(idItemCode, idSupplier);

        if (itemCodeSupplier == null) {
            return "ItemCodeSupplier does not exists!";
        }

        itemCodeSupplierRepository.delete(itemCodeSupplier);

        return "Successfully deleted ItemCodeSuppler named: " + itemCodeSupplier.getItemCodeSupplierString();
    }

    public ItemCodeSupplier findByItemCodeAndSupplier(List<String> results, ItemCode itemCode, Company supplier) {

        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierRepository.findByIdItemCodeAndIdSupplier(itemCode.getIdItemCode(), supplier.getIdCompany());

        if (itemCodeSupplier == null) {
            results.add("Could not find ItemCodeSupplier with IdCompany: " + supplier.getIdCompany() +
                    " and IdItemCode: " + itemCode.getIdItemCode());
        }

        return itemCodeSupplier;
    }

    public ItemCodeSupplier findByItemCodeSupplierString(String itemCodeSupplierString) {
        return itemCodeSupplierRepository.findByItemCodeSupplierString(itemCodeSupplierString);
    }

    public ItemCodeSupplier findByIdItemCodeSupplier(List<String> results, String idItemCodeSupplier) {

        List<Integer> id = Arrays.stream(idItemCodeSupplier.split("-")).map(Integer::parseInt).toList();
        ItemCode itemCode = itemCodeService.findByIdItemCode(results, id.get(0));
        if (itemCode == null) {
            results.add("Could not find ItemCode with Id: " + id.get(0));
            return new ItemCodeSupplier();
        }

        Company supplier = companyClusterService.findCompanyByIdCompany(results, id.get(1).toString());
        if (supplier == null) {
            results.add("Could not find Supplier with idCompany: " + id.get(1));
            return new ItemCodeSupplier();
        }

        return findByItemCodeAndSupplier(results, itemCode, supplier);
    }

    public ItemCodeSupplier findByItemCodeStringAndSupplierNameEn(String itemCodeString, String supplierNameEn) {
        return itemCodeSupplierRepository.findByItemCodeStringAndSupplierNameEn(itemCodeString, supplierNameEn);
    }
}
