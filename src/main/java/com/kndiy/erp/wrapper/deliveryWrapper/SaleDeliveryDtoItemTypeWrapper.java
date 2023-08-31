package com.kndiy.erp.wrapper.deliveryWrapper;

import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.TreeSet;

@Setter
@Getter
public class SaleDeliveryDtoItemTypeWrapper {

    private String itemTypeQuantity;

    private String itemTypeEquivalent;

    private String itemTypeEquivalentAdjusted;

    private Integer itemTypeRolls;

    private String itemTypeAmount;

    private String itemTypeOrder;

    private TreeSet<SaleDeliverySummaryDto> saleDeliverySummaryDtoTreeSet;
}
