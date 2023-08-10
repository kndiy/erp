package com.kndiy.erp.entities.giftOut;

import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "gifts_out")
@Entity
public class GiftOut {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_gift_out")
    private Integer idGiftOut;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "gifts_out_inventories_out",
            joinColumns = {@JoinColumn(name = "id_gift_out", referencedColumnName = "id_gift_out")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_out", referencedColumnName = "id_inventory_out")}
    )
    private List<InventoryOut> inventoryOutList;
}
