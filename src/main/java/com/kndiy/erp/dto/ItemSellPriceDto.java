package com.kndiy.erp.dto;

import com.kndiy.erp.validators.ItemSellPriceDtoConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ItemSellPriceDtoConstraint
@Setter
@Getter
public class ItemSellPriceDto {
    @NotNull(message = "Please select a corresponding Customer")
    private Integer idCustomer;
    private Integer idItemCode;
    private Integer idItemSellPrice;
    private Float itemSellPriceAmount;
    private String itemSellPriceUnit;
    @NotBlank(message = "Contract is required!")
    private String itemSellPriceContract;
    private String note;
    private LocalDate fromDate;
    private LocalDate toDate;

}
