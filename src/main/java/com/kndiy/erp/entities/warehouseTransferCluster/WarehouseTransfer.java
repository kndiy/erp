package com.kndiy.erp.entities.warehouseTransferCluster;

import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "warehouse_transfers")
public class WarehouseTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_warehouse_transfer")
    private Integer idWarehouseTransfer;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "warehouse_transfers_inventories_out",
            joinColumns = {@JoinColumn(name = "id_warehouse_transfer", referencedColumnName = "id_warehouse_transfer")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_out", referencedColumnName = "id_inventory_out")}
    )
    private List<InventoryOut> inventoryOutList;
}
