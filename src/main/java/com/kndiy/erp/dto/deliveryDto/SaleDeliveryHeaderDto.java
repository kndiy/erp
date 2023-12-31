package com.kndiy.erp.dto.deliveryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDeliveryHeaderDto {

    private LocalDate deliveryDate;

    private Integer deliveryTurn;

    private String saleSourceNameVn;

    private String saleSourceHQAddress;

    private String saleSourceLandLine;

    private String customerNameVn;

    private String customerHQAddress;

    private String deliverToAddressName;

    private String deliverToAddressVn;

    private String receiverName;

    private String receiverPhone;

    private Integer idSaleLot;

    private String deliveryAmount;

    private String vatRate;

    private String vat;

    private String deliveryAmountWithVat;

}
