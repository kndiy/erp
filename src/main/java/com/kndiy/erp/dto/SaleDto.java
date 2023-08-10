package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a source Company!")
    private String idCompanySource;
    private Integer idSale;
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Customer!")
    private String idCustomer;
    @NotBlank(message = "Order Name is required!")
    private String orderName;
    private String orderBatch;
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Contact!")
    private String idContactCustomer;

    private LocalDate orderDate = LocalDate.now();
    private String note;
    private Boolean doneDelivery;

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderBatch() {
        return orderBatch;
    }

    public void setOrderBatch(String orderBatch) {
        this.orderBatch = orderBatch;
    }



    public Integer getIdSale() {
        return idSale;
    }

    public void setIdSale(Integer idSale) {
        this.idSale = idSale;
    }

    public String getIdCompanySource() {
        return idCompanySource;
    }

    public void setIdCompanySource(String idCompanySource) {
        this.idCompanySource = idCompanySource;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdContactCustomer() {
        return idContactCustomer;
    }

    public void setIdContactCustomer(String idContactCustomer) {
        this.idContactCustomer = idContactCustomer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDoneDelivery() {
        return doneDelivery;
    }

    public void setDoneDelivery(Boolean doneDelivery) {
        this.doneDelivery = doneDelivery;
    }

}
