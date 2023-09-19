package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemCodeSupplierDto {
    @NotNull(message = "idItemCode must not be left blank!")
    private Integer idItemCode;
    @NotNull(message = "Please select an existing supplier, or add new Supplier on \"Company Section\"")
    private Integer idCompany;
    private Integer newIdCompany;
    @NotBlank(message = "What is the point of adding new Supplier without assigning an Item Code of that Supplier?")
    private String itemCodeSupplierString;
    private Float equivalent = 0F;

}
