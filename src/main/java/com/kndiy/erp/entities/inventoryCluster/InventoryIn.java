package com.kndiy.erp.entities.inventoryCluster;

import com.kndiy.erp.entities.companyCluster.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories_in")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryIn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_inventory_in")
    private Integer idInventoryIn;
    @Column(name = "inventory_source")
    private String inventoryInSource;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_company_source")
    private Company supplierSource;
    @Column(name = "inventory_value")
    private String inventoryInValue; //cost, price in VND
    @Column(name = "exchange_rate")
    private String exchangeRate;
    @Column(name = "inventory_value_in_foreign")
    private String inventoryInValueForeign;
    @Column(name = "foreignUnit")
    private String foreignUnit;
    @Column(name = "voucher")
    private String voucher; //contracts, ect.
    private LocalDateTime createdAt;
    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
    public static enum InventorySourceEnum {
        PURCHASE, MANUFACTURED, WAREHOUSE_TRANSFERRED, GIFTED
    }

    @Override
    public String toString() {
        return idInventoryIn.toString();
    }
}
