package com.kndiy.erp.entities.inventoryCluster;

import com.kndiy.erp.entities.giftOut.GiftOut;
import com.kndiy.erp.entities.manufactureCluster.Manufacture;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.entities.warehouseTransferCluster.WarehouseTransfer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "inventory_out")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOut implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_inventory_out")
    private Integer idInventoryOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_inventory")
    private Inventory inventory;

    @Column(name = "inventory_out_purpose")
    private String inventoryOutPurpose;

    @Column(name = "inventory_out_quantity")
    private String quantity;

    @Column(name = "equivalent_quantity")
    private String equivalent;

    @ManyToMany(mappedBy = "inventoryOutList")
    private List<SaleLot> saleLotList;

    @ManyToMany(mappedBy = "materialInList")
    private List<Manufacture> manufactureList;

    @ManyToMany(mappedBy = "inventoryOutList")
    private List<WarehouseTransfer> warehouseTransferList;

    @ManyToMany(mappedBy = "inventoryOutList")
    private List<GiftOut> giftOutList;

    public static enum InventoryOutPurposeEnum {
        SALES, MANUFACTURE, WAREHOUSE_TRANSFER, GIFT
    }


    @Override
    public String toString() {
        return idInventoryOut.toString();
    }
}
