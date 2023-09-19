package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.salesCluster.SaleContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleContainerWrapper {

    private List<SaleContainer> saleContainerList;

}
