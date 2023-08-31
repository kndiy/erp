package com.kndiy.erp.wrapper.deliveryWrapper;

import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDeliveryDtoContainerWrapper {

    private LocalDate deliveryDate;

    @NotNull(message = "Please select a delivery turn By clicking on desired Summary Grid!")
    private Integer deliveryTurn;

    private Integer idSaleLot;

    private String reportName;

    private String containerOrder;

    private String containerQuantity;

    private String containerEquivalent;

    private String containerEquivalentAdjusted;

    private String containerAmount;

    private Integer containerRolls;

    private LinkedList<SaleDeliveryDto> saleDeliveryDtoList;
}
