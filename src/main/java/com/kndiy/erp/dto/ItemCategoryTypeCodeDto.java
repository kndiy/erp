package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemCategoryTypeCodeDto {
    @NotBlank(message = "Please enter Item Category!")
    private String itemCategoryString;
    @NotBlank(message = "Please enter Item Type!")
    private String itemTypeString;
    @NotBlank(message = "Please enter Item Code!")
    private String itemCodeString;
    private Integer idItemCode;

}
