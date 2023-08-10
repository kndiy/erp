package com.kndiy.erp.services.item;

import com.kndiy.erp.dto.ItemCategoryTypeCodeDto;
import com.kndiy.erp.entities.itemCodeCluster.ItemCategory;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import com.kndiy.erp.repositories.ItemCategoryRepository;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.repositories.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;


@Service
public class ItemCodeService {
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemCategoryService itemCategoryService;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;

    public List<ItemCode> findAllItemCodes() {
        return itemCodeRepository.findAllItemCodes();
    }
    public List<ItemCode> findItemCodesByIdItemCategory(Integer idItemCategory) {
        return itemCodeRepository.findByIdItemCategory(idItemCategory);
    }
    public List<ItemCode> findItemCodesByIdItemType(Integer idItemType) {
        return itemCodeRepository.findByIdItemType(idItemType);
    }

    public ItemCode findByItemCodeString(List<String> results, String itemCodeString) {
        ItemCode itemCode = itemCodeRepository.findByItemCodeString(itemCodeString);

        if (itemCode == null) {
            results.add("Could not find ItemCode with ItemCodeString: " + itemCodeString);
        }

        return itemCode;
    }
    public String addNewItemCode(ItemCategoryTypeCodeDto itemCategoryTypeCodeDto) {

        itemCategoryTypeCodeDto.setItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString().replace(","," ").trim());
        itemCategoryTypeCodeDto.setItemTypeString(itemCategoryTypeCodeDto.getItemTypeString().replace(","," ").trim());

        ItemCode checkItemCode = itemCodeRepository.findByItemCodeString(itemCategoryTypeCodeDto.getItemCodeString());

        if (checkItemCode == null) {
            checkItemCode = new ItemCode();

            ItemType checkItemType = itemTypeService.findByItemTypeString(itemCategoryTypeCodeDto.getItemTypeString());
            ItemCategory checkItemCategory = itemCategoryService.findByItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString());

            if (checkItemCategory == null) {
                itemCategoryService.addNewItemCategory(itemCategoryTypeCodeDto.getItemCategoryString());
                checkItemCategory = itemCategoryRepository.findByItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString());
            }
            if (checkItemType == null) {
                itemTypeService.addNewItemType(checkItemCategory, itemCategoryTypeCodeDto.getItemTypeString());
                checkItemType = itemTypeRepository.findByItemTypeString(itemCategoryTypeCodeDto.getItemTypeString());
            }

            checkItemCode.setItemType(checkItemType);
            checkItemCode.setItemCodeString(itemCategoryTypeCodeDto.getItemCodeString());

            itemCodeRepository.save(checkItemCode);
        }
        else {
            return "This Item Code named " + checkItemCode.getItemCodeString() + " already exists under Item Type " + checkItemCode.getItemType().getItemTypeString() + " of Item Category " + checkItemCode.getItemType().getItemCategory().getItemCategoryString();
        }

        return "Successfully created new Item Code named: " + checkItemCode.getItemCodeString();
    }

    public String addNewItemCode(ItemType itemType, ItemCode itemCode) {

        ItemCode check = itemCodeRepository.findByItemCodeString(itemCode.getItemCodeString());

        if (check != null) {
            return "ItemCode named: " + itemCode.getItemCodeString() + " already exist!";
        }

        check = new ItemCode();
        check.setItemType(itemType);
        check.setItemCodeString(itemCode.getItemCodeString());
        check.setCreatedAt(itemCode.getCreatedAt());
        check.setNote(itemCode.getNote());

        itemCodeRepository.save(check);

        return "Successfully created new Item Code named: " + check.getItemCodeString();
    }

    public List<String> getModifiedItemCodeErrors(Errors errors) {
        List<String> res = new ArrayList<>();

        errors.getFieldErrors().forEach(fieldError -> {
            res.add(fieldError.getDefaultMessage());
        });

        return res;
    }

    public String deleteByIdItemCode(Integer idItemCode) {
        ItemCode itemCode = itemCodeRepository.findById(idItemCode).orElse(null);

        if (itemCode != null) {
            itemCodeRepository.delete(itemCode);
        }
        else {
            return "No Item Code with id: " + idItemCode + "exists!";
        }
        return "Successfully deleted Item Code named: " + itemCode.getItemCodeString();
    }

    public String editItemCode(ItemCategoryTypeCodeDto itemCategoryTypeCodeDto) {
        itemCategoryTypeCodeDto.setItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString().replace(",", " ").trim());
        itemCategoryTypeCodeDto.setItemTypeString(itemCategoryTypeCodeDto.getItemTypeString().replace(",", " ").trim());

        ItemCode itemCode = itemCodeRepository.findById(itemCategoryTypeCodeDto.getIdItemCode()).orElse(null);

        if (itemCode == null) {
            return "Item Code does not exist anymore, please create new Item Code!";
        }

        ItemCategory itemCategory = itemCategoryRepository.findByItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString());
        if (itemCategory == null) {
            itemCategoryService.addNewItemCategory(itemCategoryTypeCodeDto.getItemCategoryString());
            itemCategory = itemCategoryRepository.findByItemCategoryString(itemCategoryTypeCodeDto.getItemCategoryString());
        }

        ItemType itemType = itemTypeRepository.findByItemTypeString(itemCategoryTypeCodeDto.getItemTypeString());
        if (itemType == null) {
            itemTypeService.addNewItemType(itemCategory,itemCategoryTypeCodeDto.getItemTypeString());
            itemType = itemTypeRepository.findByItemTypeString(itemCategoryTypeCodeDto.getItemTypeString());
        }

        itemCode.setItemType(itemType);
        itemCode.setItemCodeString(itemCategoryTypeCodeDto.getItemCodeString());
        itemCodeRepository.save(itemCode);
        return "Successfully edited Item Code named: " + itemCategoryTypeCodeDto.getItemCodeString();
    }

    public ItemCode findByIdItemCode(List<String> res, Integer idItemCode) {

        ItemCode itemCode = itemCodeRepository.findById(idItemCode).orElse(null);

        if (itemCode == null) {
            res.add("ItemCode with Id: " + idItemCode + " does not exist anymore!");
        }

        return itemCode;
    }
}
