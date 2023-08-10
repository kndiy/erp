package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ItemCodeSupplierDto {
    @NotNull(message = "idItemCode must not be left blank!")
    private Integer idItemCode;
    @NotNull(message = "Please select an existing supplier, or add new Supplier on \"Company Section\"")
    private Integer idCompany;
    private Integer newIdCompany;
    @NotBlank(message = "What is the point of adding new Supplier without assigning an Item Code of that Supplier?")
    private String itemCodeSupplierString;
    private Float equivalent = 0F;

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public String getItemCodeSupplierString() {
        return itemCodeSupplierString;
    }

    public void setItemCodeSupplierString(String itemCodeSupplierString) {
        this.itemCodeSupplierString = itemCodeSupplierString;
    }

    public Float getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(Float equivalent) {
        this.equivalent = equivalent;
    }

    public Integer getNewIdCompany() {
        return newIdCompany;
    }

    public void setNewIdCompany(Integer newIdCompany) {
        this.newIdCompany = newIdCompany;
    }
}
