package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.salesCluster.SaleContainer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SaleContainerWrapper {

    List<SaleContainer> saleContainerList;

    public List<SaleContainer> getSaleContainerList() {
        return saleContainerList;
    }

    public void setSaleContainerList(List<SaleContainer> saleContainerList) {
        this.saleContainerList = saleContainerList;
    }
}
