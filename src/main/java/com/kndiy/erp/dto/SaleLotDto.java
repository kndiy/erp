package com.kndiy.erp.dto;

import com.kndiy.erp.validators.NumberStringConstraint;
import com.kndiy.erp.validators.SaleLotDtoConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SaleLotDtoConstraint
@Setter
@Getter
public class SaleLotDto implements Comparable<SaleLotDto> {

    private Integer idSaleLot;

    @NotNull(message = "Please select a SaleContainer to get its Id!")

    private Integer idSaleContainer;

    private String idFromAddress;

    private String fromAddressName;

    private String idToAddress;

    private String toAddressName;

    private String idContactReceiver;

    private String receiverName;

    private String receiverPhone;

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Supplier to set a purchase order!")

    private String idCompanySupplier;

    private String supplierNameEn;

    private LocalDate deliveryDate;

    private String deliveryTurn;

    private String lotName;

    @NumberStringConstraint(message = "OrderQuantity is not a valid number!")

    private String orderQuantity = "0";

    private String orderStyle;

    private String orderColor;

    private String note;

    private Boolean supplierSettled;

    private String quantity;

    private String equivalent;

    @Override
    public int compareTo(SaleLotDto o) {
        return lotName.compareTo(o.lotName);
    }
}
