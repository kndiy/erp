package com.kndiy.erp.entities.itemCodeCluster;

import com.kndiy.erp.entities.companyCluster.Company;
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
import java.util.SortedSet;

@Entity
@Table(name = "item_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCode implements Serializable, Comparable<ItemCode> {
    @Id
    @Column(name = "id_item_code")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idItemCode;
    @JoinColumn(name = "id_item_type", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemType itemType;
    @Column(name = "item_code")
    private String itemCodeString;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "item_codes_suppliers",
            joinColumns = {@JoinColumn(name = "id_item_code", referencedColumnName = "id_item_code")},
            inverseJoinColumns = {@JoinColumn(name = "id_company", referencedColumnName = "id_company")}
    )
    private List<Company> supplierList;
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "itemCode") //reference to ItemCodeSupplier.itemCode
    private List<ItemCodeSupplier> itemCodeSuppliers;
    @Column(name = "note")
    private String note;
    @Fetch(FetchMode.JOIN)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "item_codes_sell_price",
            joinColumns = {@JoinColumn(name = "id_item_code", referencedColumnName = "id_item_code")},
            inverseJoinColumns = {@JoinColumn(name = "id_item_sell_price", referencedColumnName = "id_item_sell_price")}
    )
    private SortedSet<ItemSellPrice> itemSellPriceList;
    private LocalDateTime createdAt;
    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return idItemCode.toString();
    }

    @Override
    public int compareTo(ItemCode itemCode) {
        return this.getItemCodeString().compareTo(itemCode.getItemCodeString());
    }
}
