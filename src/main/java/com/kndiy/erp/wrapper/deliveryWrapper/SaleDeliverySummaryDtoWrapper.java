package com.kndiy.erp.wrapper.deliveryWrapper;

import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.TreeSet;

@Setter
@Getter
public class SaleDeliverySummaryDtoWrapper {

    private String deliveryQuantity;

    private String deliveryEquivalent;

    private Integer deliveryRolls;

    private TreeSet<SaleDeliverySummaryDto> saleDeliverySummaryDtoTreeSet;
}
