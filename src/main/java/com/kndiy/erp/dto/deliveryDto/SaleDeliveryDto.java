package com.kndiy.erp.dto.deliveryDto;

import com.kndiy.erp.dto.InventoryOutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleDeliveryDto {

    private LocalDate deliveryDate;

    private String departFrom;

    private Integer deliveryTurn;

    private String deliverTo;

    private Integer idSale;

    private String saleSource;

    private String customer;

    private String orderName;

    private String orderBatch;

    private Integer idSaleArticle;

    private String itemType;

    private String itemCode;

    private Integer idSaleContainer;

    private String container;

    private Integer idSaleLot;

    private String lotName;

    private String lotOrder;

    private String lotQuantity;

    private String lotEquivalent;

    private String lotEquivalentAdjusted;

    private String lotAmount;

    private String itemSellPrice;

    private Integer lotRolls;

    private String lotColor;

    private String lotStyle;

    private String lotItemCodeSupplierString;

    private String supplierAbbreviation;

    private String lotNote;

    private TreeSet<InventoryOutDto> inventoryOutDtoTreeSet;
}
