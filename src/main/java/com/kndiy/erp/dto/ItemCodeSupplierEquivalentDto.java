package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

}
