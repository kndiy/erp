package com.kndiy.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummaryQuantityDto {

    private String orderQuantity;
    private String allowedQuantity;
    private String deliveredQuantity;
    private String differential;
    private String percentageDiff;
    private String allowedSurplus;
    private String saleUnit;

}
