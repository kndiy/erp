package com.kndiy.erp.entities.manufactureCluster;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "manufactures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_manufacture")
    private Integer idManufacture;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_address", nullable = false)
    private Address factoryAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_item_type", nullable = false)
    private ItemType productType;
    @Column(name = "manufacture_order")
    private String manufactureOrder;
    @Column(name = "from_time")
    private LocalDateTime fromDateTime;
    @Column(name = "to_time")
    private LocalDateTime toDateTime;
    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "manufactures_inventories_in",
            joinColumns = {@JoinColumn(name = "id_manufacture", referencedColumnName = "id_manufacture")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_in", referencedColumnName = "id_inventory_in")}
    )
    private List<InventoryIn> productOutList; //products after manufacturing
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "manufactures_inventories_out",
            joinColumns = {@JoinColumn(name = "id_manufacture", referencedColumnName = "id_manufacture")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_out", referencedColumnName = "id_inventory_out")}
    )
    private List<InventoryOut> materialInList; //input materials

    @Override
    public String toString() {
        return idManufacture.toString();
    }
}

