package com.kndiy.erp.entities.inventoryCluster;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"production_code", "number_in_batch"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable, Comparable<Inventory> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_inventory")
    private Integer idInventory;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_item_code", nullable = false)
    private ItemCode itemCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_inventory_in", nullable = false)
    private InventoryIn inventoryIn;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_address", nullable = false)
    private Address storedAtAddress;

    @OneToMany(mappedBy = "inventory")
    private List<InventoryOut> inventoryOutList;

    @Column(name = "placement_in_warehouse")
    private String placementInWarehouse;
    @Column(name = "production_code", nullable = false)
    private String productionCode;
    @Column(name = "number_in_batch", nullable = false)
    private Integer numberInBatch;
    @Column(name = "init_quantity")
    private String initQuantity;
    @Column(name = "remaining_quantity")
    private String remainingQuantity;

    @Override
    public String toString() {
        return idInventory.toString();
    }

    @Override
    public int compareTo(Inventory o) {
        return numberInBatch.compareTo(o.numberInBatch);
    }
}
