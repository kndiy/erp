package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleArticleDto implements Comparable<SaleArticleDto> {

    @NotNull(message = "Please select one row on Sale Data table to get its Id!")
    private Integer idSale;

    private Integer idSaleArticle;

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please select corresponding ItemCode!")
    private String idItemCode;

    @NotNull(message = "Please enter the ETD!")
    private LocalDate requestDeliveryDate;

    @Pattern(regexp = "^0[.][0-9][0-9]$", message = "Please enter allowed surplus number to calculate Settled Quantity!")
    private String allowedSurplus = "0.03";

    private String quantity;

    private String equivalent;

    @Override
    public int compareTo(SaleArticleDto o) {
        return idSaleArticle.compareTo(o.idSaleArticle);
    }
}
