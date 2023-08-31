package com.kndiy.erp.entities.itemCodeCluster;

import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entityCompositeKeys.ItemCodeSupplierKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "item_codes_suppliers")
@AllArgsConstructor
@NoArgsConstructor
public class ItemCodeSupplier implements Serializable {

    @EmbeddedId
    private ItemCodeSupplierKey idItemCodeSupplier;

    @ManyToOne
    @MapsId("idItemCode")
    @JoinColumn(name = "id_item_code")
    private ItemCode itemCode;

    @ManyToOne
    @MapsId("idCompany")
    @JoinColumn(name = "id_company")
    private Company supplier;

    @Column(name = "item_code_supplier")
    private String itemCodeSupplierString;

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "itemCodeSupplier")
    private List<ItemCodeSupplierEquivalent> itemCodeSupplierEquivalentList;

    @Override
    public String toString() {
        return idItemCodeSupplier.toString();
    }
}
