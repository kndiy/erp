package com.kndiy.erp.dto.deliveryDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDeliverySummaryDto implements Comparable<SaleDeliverySummaryDto> {

    private String orderName;

    private String itemCodeString;

    private String container;

    private Integer containerRolls;

    private String containerQuantity;

    private String containerEquivalent;

    @Override
    public int compareTo(SaleDeliverySummaryDto o) {
        if (!orderName.equals(o.orderName)) {
            return orderName.compareTo(o.orderName);
        }
        else if (!itemCodeString.equals(o.itemCodeString)) {
            return itemCodeString.compareTo(o.itemCodeString);
        }
        return container.compareTo(o.container);
    }
}
