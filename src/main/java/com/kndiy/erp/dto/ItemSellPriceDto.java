package com.kndiy.erp.dto;

import com.kndiy.erp.validators.ItemSellPriceDtoConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ItemSellPriceDtoConstraint
public class ItemSellPriceDto {
    @NotNull(message = "Please select a corresponding Customer")
    private Integer idCustomer;
    private Integer idItemCode;
    private Integer idItemSellPrice;
    private Float itemSellPriceAmount;
    private String itemSellPriceUnit;
    private String itemSellPriceContract;
    private String note;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }

    public Float getItemSellPriceAmount() {
        return itemSellPriceAmount;
    }

    public void setItemSellPriceAmount(Float itemSellPriceAmount) {
        this.itemSellPriceAmount = itemSellPriceAmount;
    }

    public String getItemSellPriceUnit() {
        return itemSellPriceUnit;
    }

    public void setItemSellPriceUnit(String itemSellPriceUnit) {
        this.itemSellPriceUnit = itemSellPriceUnit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getItemSellPriceContract() {
        return itemSellPriceContract;
    }

    public void setItemSellPriceContract(String itemSellPriceContract) {
        this.itemSellPriceContract = itemSellPriceContract;
    }

    public Integer getIdItemSellPrice() {
        return idItemSellPrice;
    }

    public void setIdItemSellPrice(Integer idItemSellPrice) {
        this.idItemSellPrice = idItemSellPrice;
    }
}
