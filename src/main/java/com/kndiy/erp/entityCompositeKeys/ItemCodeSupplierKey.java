package com.kndiy.erp.entityCompositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCodeSupplierKey implements Serializable {
    @Column(name = "id_company")
    private Integer idCompany;
    @Column(name = "id_item_code")
    private Integer idItemCode;

    public Integer getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Integer idCompany) {
        this.idCompany = idCompany;
    }

    public Integer getIdItemCode() {
        return idItemCode;
    }

    public void setIdItemCode(Integer idItemCode) {
        this.idItemCode = idItemCode;
    }

    @Override
    public String toString() {
        return idItemCode.toString() + "-" + idCompany.toString();
    }
}
