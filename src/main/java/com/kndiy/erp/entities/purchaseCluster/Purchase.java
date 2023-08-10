package com.kndiy.erp.entities.purchaseCluster;

import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "purchases")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_purchase")
    private Integer idPurchase;
    @Column(name = "purchase_order")
    private String purchaseOrder; //order that authorized this purchase
    @Column(name = "purchase_contract")
    private String purchaseContract;
    @Column(name = "purchase_red_invoice")
    private String purchaseRedInvoice;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "purchases_inventories_in",
            joinColumns = {@JoinColumn(name = "id_purchase", referencedColumnName = "id_purchase")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_in", referencedColumnName = "id_inventory_in")}
    )
    private List<InventoryIn> purchasedInventoryList;

    @Override
    public String toString() {
        return idPurchase.toString();
    }
}
