package com.kndiy.erp.entities.itemCodeCluster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
@Entity
@Table(name = "item_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_item_type")
    private Integer idItemType;
    @JoinColumn(name = "id_category", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemCategory itemCategory;
    @Column(name = "item_type", unique = true)
    private String itemTypeString;

    @Override
    public String toString() {
        return idItemType.toString();
    }
}
