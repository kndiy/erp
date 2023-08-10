package com.kndiy.erp.entities.itemCodeCluster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item-code-supplier-equivalent")
public class ItemCodeSupplierEquivalent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_item_code_supplier_equivalent")
    private Integer idItemCodeSupplierEquivalent;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumns({
            @JoinColumn(name = "id_item_code"),
            @JoinColumn(name = "id_company")
    })
    private ItemCodeSupplier itemCodeSupplier;
    @Column(name = "equivalent")
    private String equivalent;
    @Column(name = "equivalent_unit")
    private String equivalentUnit;
    @Column(name = "source_unit")
    private String sourceUnit;

    @Override
    public String toString() {
        return this.idItemCodeSupplierEquivalent.toString();
    }
}
