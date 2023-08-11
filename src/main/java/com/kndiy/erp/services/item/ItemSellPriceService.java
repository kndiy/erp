package com.kndiy.erp.services.item;

import com.kndiy.erp.dto.ItemSellPriceDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemSellPrice;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.repositories.ItemSellPriceRepository;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.*;

@Service
public class ItemSellPriceService {
    @Autowired
    private ItemSellPriceRepository itemSellPriceRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private Validator validator;
    public List<String> getModifiedItemCodeSupplierErrors(Errors errors) {
        List<String> res = new ArrayList<>();
        errors.getAllErrors().forEach(error -> {
            res.add(error.getDefaultMessage());
        });
        return res;
    }
    public List<ItemSellPrice> findAll(){
        return itemSellPriceRepository.findAll();
    }

    public String addNewItemSellPrice(ItemSellPriceDto itemSellPriceDto) {
        Company customer = companyRepository.findById(itemSellPriceDto.getIdCustomer()).orElse(null);
        ItemCode itemCode = itemCodeRepository.findById(itemSellPriceDto.getIdItemCode()).orElse(null);
        ItemSellPrice itemSellPrice;
        itemSellPriceDto.setItemSellPriceContract(itemSellPriceDto.getItemSellPriceContract().replace(",", " ").trim());

        try {
            Integer idItemSellPrice = Integer.parseInt(itemSellPriceDto.getItemSellPriceContract());
            itemSellPrice = itemSellPriceRepository.findById(idItemSellPrice).orElse(null);
            itemCode.getItemSellPriceList().add(itemSellPrice);
            itemCodeRepository.save(itemCode);

            return "Successfully linked Sell Price of Contract: " + itemSellPrice.getItemSellPriceContract() + " to ItemCode named: " + itemCode.getItemCodeString();
        }
        catch (Exception ex) {
            itemSellPrice = itemSellPriceRepository.findByContract(itemSellPriceDto.getItemSellPriceContract());

            if (itemSellPrice != null) {

                itemCode.getItemSellPriceList().add(itemSellPrice);
                itemCodeRepository.save(itemCode);

                return "Successfully linked Sell Price of Contract: " + itemSellPrice.getItemSellPriceContract() + " to ItemCode named: " + itemCode.getItemCodeString();
            }

            itemSellPrice = new ItemSellPrice();

            itemSellPrice.setItemSellPriceContract(itemSellPriceDto.getItemSellPriceContract());
            itemSellPrice.setFromDate(itemSellPriceDto.getFromDate());
            itemSellPrice.setToDate(itemSellPriceDto.getToDate());
            itemSellPrice.setCustomer(customer);
            itemSellPrice.setItemSellPriceUnit(itemSellPriceDto.getItemSellPriceUnit());
            itemSellPrice.setItemSellPriceAmount(itemSellPriceDto.getItemSellPriceAmount());
            itemSellPrice.setNote(itemSellPriceDto.getNote());
            itemSellPriceRepository.save(itemSellPrice);
            itemCode.getItemSellPriceList().add(itemSellPrice);
            itemCodeRepository.save(itemCode);
        }
        return "Successfully created new Item Sell Price of Item Code: " + itemCode.getItemCodeString() ;
    }

    public String restoreItemSellPrice(ItemSellPrice itemSellPrice, List<ItemCode> itemCodes) {

        itemSellPriceRepository.save(itemSellPrice);

        for (ItemCode itemCode : itemCodes) {
            SortedSet<ItemSellPrice> itemSellPriceList = itemCode.getItemSellPriceList();

            if (itemSellPriceList == null) {
                itemSellPriceList = new TreeSet<>();
                itemSellPriceList.add(itemSellPrice);
                itemCode.setItemSellPriceList(itemSellPriceList);
            }
            else {
                itemCode.getItemSellPriceList().add(itemSellPrice);
            }
            itemCodeRepository.save(itemCode);
        }

        return "Successfully restored ItemSellPrice with contract named: " + itemSellPrice.getItemSellPriceContract();
    }

    public String deleteByIdItemSellPrice(Integer idItemSellPrice) {
        ItemSellPrice check = itemSellPriceRepository.findById(idItemSellPrice).orElse(null);

        if (check == null) {
            return "Contract does not exists!";
        }
        List<ItemCode> itemCodeList = itemCodeRepository.findByItemSellPrice(check);

        for (ItemCode ic : itemCodeList) {
            ic.getItemSellPriceList().remove(check);
        }

        itemSellPriceRepository.delete(check);
        return "Successfully deleted Contract named: " + check.getItemSellPriceContract();
    }

    public String editSalesContract(ItemSellPriceDto itemSellPriceDto) {
        ItemSellPrice oldItemSellPrice = itemSellPriceRepository.findById(itemSellPriceDto.getIdItemSellPrice()).orElse(null);

        if (oldItemSellPrice == null) {
            return "Contract named: " + itemSellPriceDto.getItemSellPriceContract() + " does not exist anymore!";
        }

        Company customer = companyRepository.findById(itemSellPriceDto.getIdCustomer()).orElse(null);

        ItemSellPrice newItemSellPrice = new ItemSellPrice();
        newItemSellPrice.setIdItemSellPrice(itemSellPriceDto.getIdItemSellPrice());
        newItemSellPrice.setCustomer(customer);
        newItemSellPrice.setItemSellPriceContract(itemSellPriceDto.getItemSellPriceContract());
        newItemSellPrice.setFromDate(itemSellPriceDto.getFromDate());
        newItemSellPrice.setToDate(itemSellPriceDto.getToDate());
        newItemSellPrice.setItemSellPriceAmount(itemSellPriceDto.getItemSellPriceAmount());
        newItemSellPrice.setItemSellPriceUnit(itemSellPriceDto.getItemSellPriceUnit());
        itemSellPriceRepository.save(newItemSellPrice);

        List<ItemCode> itemCodeLinkedToThisContract = itemCodeRepository.findByItemSellPrice(oldItemSellPrice);

        for (ItemCode itemCode : itemCodeLinkedToThisContract) {
            itemCode.getItemSellPriceList().remove(oldItemSellPrice);
            itemCode.getItemSellPriceList().add(newItemSellPrice);

            itemCodeRepository.save(itemCode);
        }

        return "Successfully edited Contract name: ";
    }

    public String removeLinkedContract(Integer idItemSellPrice, Integer idItemCode) {

        ItemSellPrice itemSellPrice = itemSellPriceRepository.findById(idItemSellPrice).orElse(null);
        ItemCode itemCode = itemCodeRepository.findById(idItemCode).orElse(null);

        if (itemSellPrice == null) {
            return "This Contract does not exist anymore!";
        }
        if (itemCode == null) {
            return "This ItemCode does not exist anymore!";
        }

        itemCode.getItemSellPriceList().remove(itemSellPrice);
        itemCodeRepository.save(itemCode);

        return "Successfully remove the link between Contract named: " + itemSellPrice.getItemSellPriceContract() + " from ItemCode named: " +itemCode.getItemCodeString();
    }

    public ItemSellPrice findByItemSellPriceContract(List<String> results, String contract) {
        ItemSellPrice itemSellPrice = itemSellPriceRepository.findByContract(contract);
        if (itemSellPrice == null) {
            results.add("ItemSellPrice with Contract: " + contract + " has not been created!");
        }
        return itemSellPrice;
    }
}
