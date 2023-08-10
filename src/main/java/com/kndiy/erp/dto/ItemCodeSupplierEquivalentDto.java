package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ItemCodeSupplierEquivalentDto {

    private Integer idItemCode;
    private Integer idSupplier;
    private String idItemCodeSupplier;
    private Integer idItemCodeSupplierEquivalent;
    @Pattern(regexp = "^[^0-9]*$", message = "Please select or input new Source Unit!")
    private String sourceUnit;
    @Pattern(regexp = "^[^0-9]*$", message = "Please select or input new Equivalent Unit!")
    private String equivalentUnit;
    @Pattern(regexp = "^?[0-9]*[.]?[0-9]+$", message = "Please enter a valid Floating point Equivalent!")
    private String equivalent;

    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public String getEquivalentUnit() {
        return equivalentUnit;
    }

    public void setEquivalentUnit(String equivalentUnit) {
        this.equivalentUnit = equivalentUnit;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }

    public Integer getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Integer idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getIdItemCodeSupplier() {
        return idItemCodeSupplier;
    }

    public void setIdItemCodeSupplier(String idItemCodeSupplier) {
        this.idItemCodeSupplier = idItemCodeSupplier;
    }

    public Integer getIdItemCodeSupplierEquivalent() {
        return idItemCodeSupplierEquivalent;
    }

    public void setIdItemCodeSupplierEquivalent(Integer idItemCodeSupplierEquivalent) {
        this.idItemCodeSupplierEquivalent = idItemCodeSupplierEquivalent;
    }
}
