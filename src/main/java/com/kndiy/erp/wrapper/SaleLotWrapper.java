package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.salesCluster.SaleLot;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SaleLotWrapper {

    private List<SaleLot> saleLotList;
    public List<SaleLot> getSaleLotList() {
        return saleLotList;
    }

    public void setSaleLotList(List<SaleLot> saleLotList) {
        this.saleLotList = saleLotList;
    }
}
