package com.kndiy.erp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class SaleDeliveryDto {
    private LocalDate deliveryDate;
    private String departFrom;
    private Integer turnNumber;
    private String deliverTo;
    private Integer idSale;
    private String orderName;
    private Integer idSaleArticle;
    private String itemType;
    private String itemCode;
    private Integer idSaleContainer;
    private String container;
    private Integer idSaleLot;
    private String lotName;

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public String getDepartFrom() {
        return departFrom;
    }

    public void setDepartFrom(String departFrom) {
        this.departFrom = departFrom;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Integer getIdSaleArticle() {
        return idSaleArticle;
    }

    public void setIdSaleArticle(Integer idSaleArticle) {
        this.idSaleArticle = idSaleArticle;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getIdSaleContainer() {
        return idSaleContainer;
    }

    public void setIdSaleContainer(Integer idSaleContainer) {
        this.idSaleContainer = idSaleContainer;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public Integer getIdSaleLot() {
        return idSaleLot;
    }

    public void setIdSaleLot(Integer idSaleLot) {
        this.idSaleLot = idSaleLot;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }
}
