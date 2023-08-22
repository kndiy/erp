package com.kndiy.erp.dto;

import com.kndiy.erp.entities.salesCluster.Sale;
import jakarta.validation.constraints.NotBlank;
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
public class SaleDto implements Comparable<SaleDto> {

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a source Company!")
    private String idCompanySource;

    private Integer idSale;

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Customer!")
    private String idCustomer;

    @NotBlank(message = "Order Name is required!")
    private String orderName;

    private String orderBatch;

    @Pattern(regexp = "^[1-9][0-9]*$", message = "Please choose a Contact!")
    private String idContactCustomer;


    private LocalDate orderDate = LocalDate.now();

    private String note;

    private Boolean doneDelivery;

    private String quantity;

    private String equivalent;


    @Override
    public int compareTo(SaleDto o) {
        return orderName.compareTo(o.orderName);
    }
}
