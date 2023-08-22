package com.kndiy.erp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleContainerDto implements Comparable<SaleContainerDto> {

    @NotNull(message = "Please select the corresponding SaleArticle!")
    private Integer idSaleArticle;

    private Integer idSaleContainer;

    private String container;

    @Pattern(regexp = "^[^0-9]*$", message = "Please choose a Unit!")

    private String orderUnit;

    private Boolean forClaim;

    private String quantity;

    private String equivalent;

    @Override
    public int compareTo(SaleContainerDto o) {
        return container.compareTo(o.container);
    }
}
