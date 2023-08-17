package com.kndiy.erp.dto;

import com.kndiy.erp.validators.NumberStringConstraint;
import com.kndiy.erp.validators.SaleLotDtoConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SaleLotDtoConstraint
public class SaleLotDto {

    private Integer idSaleLot;
    @NotNull(message = "Please select a SaleContainer to get its Id!")
    private Integer idSaleContainer;
    private String idFromAddress;
    private String idToAddress;
    private String idContactReceiver;
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Supplier to set a purchase order!")
    private String idCompanySupplier;
    private LocalDate deliveryDate;
    private String deliveryTurn;
    private String lotName;
    @NumberStringConstraint(message = "OrderQuantity is not a valid number!")
    private String orderQuantity = "0";
    private String orderStyle;
    private String orderColor;
    private String note;
    private Boolean supplierSettled;
    private String deliveredQuantity;

    public String getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(String deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public Boolean getSupplierSettled() {
        return supplierSettled;
    }

    public void setSupplierSettled(Boolean supplierSettled) {
        this.supplierSettled = supplierSettled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIdSaleLot() {
        return idSaleLot;
    }

    public void setIdSaleLot(Integer idSaleLot) {
        this.idSaleLot = idSaleLot;
    }

    public Integer getIdSaleContainer() {
        return idSaleContainer;
    }

    public void setIdSaleContainer(Integer idSaleContainer) {
        this.idSaleContainer = idSaleContainer;
    }

    public String getIdFromAddress() {
        return idFromAddress;
    }

    public void setIdFromAddress(String idFromAddress) {
        this.idFromAddress = idFromAddress;
    }

    public String getIdToAddress() {
        return idToAddress;
    }

    public void setIdToAddress(String idToAddress) {
        this.idToAddress = idToAddress;
    }

    public String getIdContactReceiver() {
        return idContactReceiver;
    }

    public void setIdContactReceiver(String idContactReceiver) {
        this.idContactReceiver = idContactReceiver;
    }

    public String getIdCompanySupplier() {
        return idCompanySupplier;
    }

    public void setIdCompanySupplier(String idCompanySupplier) {
        this.idCompanySupplier = idCompanySupplier;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTurn() {
        return deliveryTurn;
    }

    public void setDeliveryTurn(String deliveryTurn) {
        this.deliveryTurn = deliveryTurn;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderStyle() {
        return orderStyle;
    }

    public void setOrderStyle(String orderStyle) {
        this.orderStyle = orderStyle;
    }

    public String getOrderColor() {
        return orderColor;
    }

    public void setOrderColor(String orderColor) {
        this.orderColor = orderColor;
    }
}
