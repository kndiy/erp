package com.kndiy.erp.services.item;

import com.kndiy.erp.entities.itemCodeCluster.ItemCategory;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import com.kndiy.erp.repositories.ItemCategoryRepository;
import com.kndiy.erp.repositories.ItemTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCategoryService {
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;

    public List<ItemCategory> findAllItemCategories() {
        return itemCategoryRepository.findAll();
    }

    public ItemCategory findByItemCategoryString(String itemCategoryString) {
        return itemCategoryRepository.findByItemCategoryString(itemCategoryString);
    }

    public String addNewItemCategory(String itemCategoryString) {

        ItemCategory itemCategory = itemCategoryRepository.findByItemCategoryString(itemCategoryString);

        if (itemCategory != null) {
            return "Item Category named: " + itemCategoryString + " already exists!";
        }

        itemCategory = new ItemCategory();
        itemCategory.setItemCategoryString(itemCategoryString);
        itemCategoryRepository.save(itemCategory);

        return "Successfully created new Item Category named: " + itemCategoryString;
    }

    public String deleteByIdItemCategory(Integer idItemCategory) {
        ItemCategory itemCategory = itemCategoryRepository.findById(idItemCategory).orElse(null);
        if (itemCategory == null) {
            return "Item Category with id: " + idItemCategory + " does not exist!";
        }

        List<ItemType> itemTypes = itemTypeRepository.findByIdItemCategory(idItemCategory);
        if (itemTypes.isEmpty()) {
            itemCategoryRepository.deleteById(idItemCategory);
        }
        else {
            StringBuilder concat = new StringBuilder();

            for (ItemType itemType : itemTypes) {
                concat.append(itemType.getItemTypeString()).append("; ");
            }

            return "This Item Category still have Item Type children: " + concat.toString() + "\nPlease delete all child Item Types before delete this Item Category!";
        }
        return "Successfully deleted Item Category named: " + itemCategory.getItemCategoryString();
    }
}
