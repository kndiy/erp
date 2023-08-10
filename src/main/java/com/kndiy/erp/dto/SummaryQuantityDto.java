package com.kndiy.erp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SummaryQuantityDto {

    private String orderQuantity;
    private String allowedQuantity;
    private String deliveredQuantity;
    private String differential;
    private String percentageDiff;
    private String allowedSurplus;
    private String saleUnit;

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getAllowedQuantity() {
        return allowedQuantity;
    }

    public void setAllowedQuantity(String allowedQuantity) {
        this.allowedQuantity = allowedQuantity;
    }

    public String getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(String deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public String getDifferential() {
        return differential;
    }

    public void setDifferential(String differential) {
        this.differential = differential;
    }

    public String getPercentageDiff() {
        return percentageDiff;
    }

    public void setPercentageDiff(String percentageDiff) {
        this.percentageDiff = percentageDiff;
    }

    public String getAllowedSurplus() {
        return allowedSurplus;
    }

    public void setAllowedSurplus(String allowedSurplus) {
        this.allowedSurplus = allowedSurplus;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }
}
