package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
public class SaleArticleDto {

    @NotNull(message = "Please select one row on Sale Data table to get its Id!")
    private Integer idSale;
    private Integer idSaleArticle;
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please select corresponding ItemCode!")
    private String idItemCode;
    @NotNull(message = "Please enter the ETD!")
    private LocalDate requestDeliveryDate;
    @Pattern(regexp = "^0[.][0-9][0-9]$", message = "Please enter allowed surplus number to calculate Settled Quantity!")
    private String allowedSurplus = "0.03";

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public Integer getIdSaleArticle() {
        return idSaleArticle;
    }

    public void setIdSaleArticle(Integer idSaleArticle) {
        this.idSaleArticle = idSaleArticle;
    }

    public String getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(String idItemCode) {
        this.idItemCode = idItemCode;
    }

    public LocalDate getRequestDeliveryDate() {
        return requestDeliveryDate;
    }

    public void setRequestDeliveryDate(LocalDate requestDeliveryDate) {
        this.requestDeliveryDate = requestDeliveryDate;
    }

    public String getAllowedSurplus() {
        return allowedSurplus;
    }

    public void setAllowedSurplus(String allowSurplus) {
        this.allowedSurplus = allowSurplus;
    }
}
