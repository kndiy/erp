package com.kndiy.erp.wrapper;

import com.kndiy.erp.dto.InventoryDto;
import com.kndiy.erp.validators.InventoryDtoWrapperDtoConstraint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@AllArgsConstructor
@NoArgsConstructor
@InventoryDtoWrapperDtoConstraint
public class InventoryDtoWrapperDto {
    private LinkedList<InventoryDto> inventoryDtoList;
    private String newArticleNumber;
    private Integer idInventoryIn;

    public InventoryDtoWrapperDto(LinkedList<InventoryDto> inventoryDtoList) {
        this.inventoryDtoList = inventoryDtoList;
    }

    public Integer getIdInventoryIn() {
        return idInventoryIn;
    }

    public void setIdInventoryIn(Integer idInventoryIn) {
        this.idInventoryIn = idInventoryIn;
    }

    public LinkedList<InventoryDto> getInventoryDtoList() {
        return inventoryDtoList;
    }

    public void setInventoryDtoList(LinkedList<InventoryDto> inventoryDtoList) {
        this.inventoryDtoList = inventoryDtoList;
    }

    public String getNewArticleNumber() {
        return newArticleNumber;
    }

    public void setNewArticleNumber(String newArticleNumber) {
        this.newArticleNumber = newArticleNumber;
    }
}

