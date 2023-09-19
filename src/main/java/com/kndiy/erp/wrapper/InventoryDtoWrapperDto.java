package com.kndiy.erp.wrapper;

import com.kndiy.erp.dto.InventoryDto;
import com.kndiy.erp.validators.InventoryDtoWrapperDtoConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;

@AllArgsConstructor
@NoArgsConstructor
@InventoryDtoWrapperDtoConstraint
@Getter
@Setter
public class InventoryDtoWrapperDto {
    private LinkedList<InventoryDto> inventoryDtoList;
    private String newArticleNumber;
    private Integer idInventoryIn;

}

