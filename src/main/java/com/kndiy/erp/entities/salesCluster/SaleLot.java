package com.kndiy.erp.entities.salesCluster;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sale_lots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleLot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_sale_lot")
    private Integer idSaleLot;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sale_container")
    private SaleContainer saleContainer;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_company")
    private Company supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_address_from")
    private Address fromAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_address_to")
    private Address toAddress;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_contact_receiver")
    private Contact receiver;
    @Column(name = "lot_name")
    private String lotName;
    @Column(name = "order_quantity")
    private String orderQuantity;
    @Column(name = "order_style")
    private String orderStyle;
    @Column(name = "order_color")
    private String orderColor;
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    @Column(name = "delivery_turn")
    private Integer deliveryTurn;
    @Column(name = "note")
    private String note;
    @Column(name = "supplier_settled")
    private Boolean supplierSettled;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "sale_lots_inventories_out",
            joinColumns = {@JoinColumn(name = "id_sale_lot", referencedColumnName = "id_sale_lot")},
            inverseJoinColumns = {@JoinColumn(name = "id_inventory_out", referencedColumnName = "id_inventory_out")}
    )
    private List<InventoryOut> inventoryOutList;

    @Override
    public String toString() {
        return idSaleLot.toString();
    }
}
