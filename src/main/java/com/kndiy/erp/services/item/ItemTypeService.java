package com.kndiy.erp.services.item;

import com.kndiy.erp.entities.itemCodeCluster.ItemCategory;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.repositories.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;

    public List<ItemType> findAllItemTypes() {
        return itemTypeRepository.findAll();
    }

    public ItemType findByItemTypeString(String itemTypeString) {
        return itemTypeRepository.findByItemTypeString(itemTypeString);
    }

    public String addNewItemType(ItemCategory itemCategory, String itemTypeString) {

        ItemType itemType = itemTypeRepository.findByItemTypeString(itemTypeString);

        if (itemType != null) {
            return "Item Type named:" + itemTypeString + " already existed!";
        }

        itemType = new ItemType();
        itemType.setItemCategory(itemCategory);
        itemType.setItemTypeString(itemTypeString);
        itemTypeRepository.save(itemType);
        return "Successfully created new Item Type!";
    }

    public String deleteByIdItemType(Integer idItemType) {
        ItemType itemType = itemTypeRepository.findById(idItemType).orElse(null);
        if (itemType == null) {
            return "Item Type with id: " + idItemType + " does not exist!";
        }

        List<ItemCode> itemCodes = itemCodeRepository.findByIdItemType(idItemType);

        if (itemCodes.isEmpty()) {
            itemTypeRepository.deleteById(idItemType);
        }
        else {
            StringBuilder concat = new StringBuilder();

            for (ItemCode itemCode : itemCodes) {
                concat.append(itemCode.getItemCodeString()).append("; ");
            }

            return "This Item Type still have Item Code children: " + concat.toString() + "\nPlease delete all child Item Codes before delete this Item Type!";
        }
        return "Successfully deleted Item Type named: " + itemType.getItemTypeString();
    }
}
