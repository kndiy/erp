package com.kndiy.erp.wrapper.deliveryWrapper;

import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.validators.SaleDeliveryDtoWrapperConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SaleDeliveryDtoWrapperConstraint
public class SaleDeliveryDtoWrapper {

    @NotNull(message = "Please select a delivery date By clicking on desired Summary Grid!")
    private LocalDate deliveryDate;

    @NotNull(message = "Please select a delivery turn By clicking on desired Summary Grid!")
    private Integer deliveryTurn;

    private Integer idSaleLot;

    private String reportName;

    private String containerQuantity;

    private String containerEquivalent;

    private Integer containerRolls;

    private List<SaleDeliveryDto> saleDeliveryDtoList;
}
