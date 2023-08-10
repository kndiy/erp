package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ItemCategoryTypeCodeDto {
    @NotBlank(message = "Please enter Item Category!")
    private String itemCategoryString;
    @NotBlank(message = "Please enter Item Type!")
    private String itemTypeString;
    @NotBlank(message = "Please enter Item Code!")
    private String itemCodeString;
    private Integer idItemCode;
    public String getItemCategoryString() {
        return itemCategoryString;
    }

    public String getItemTypeString() {
        return itemTypeString;
    }

    public String getItemCodeString() {
        return itemCodeString;
    }

    public void setItemCategoryString(String itemCategoryString) {
        this.itemCategoryString = itemCategoryString;
    }

    public void setItemTypeString(String itemTypeString) {
        this.itemTypeString = itemTypeString;
    }

    public void setItemCodeString(String itemCodeString) {
        this.itemCodeString = itemCodeString;
    }

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }
}
