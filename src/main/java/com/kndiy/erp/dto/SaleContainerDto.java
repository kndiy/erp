package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SaleContainerDto {
    @NotNull(message = "Please select the corresponding SaleArticle!")
    private Integer idSaleArticle;
    private Integer idSaleContainer;
    private String container;
    @Pattern(regexp = "^[^0-9]*$", message = "Please choose a Unit!")
    private String orderUnit;
    private Boolean forClaim;

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
    }

    public Boolean getForClaim() {
        return forClaim;
    }

    public void setForClaim(Boolean forClaim) {
        this.forClaim = forClaim;
    }

    public Integer getIdSaleArticle() {
        return idSaleArticle;
    }

    public void setIdSaleArticle(Integer idSaleArticle) {
        this.idSaleArticle = idSaleArticle;
    }

    public Integer getIdSaleContainer() {
        return idSaleContainer;
    }

    public void setIdSaleContainer(Integer idSaleContainer) {
        this.idSaleContainer = idSaleContainer;
    }
}
