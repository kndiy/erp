package com.kndiy.erp.entities.itemCodeCluster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "item_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_item_category")
    private Integer idItemCategory;
    @Column(name = "item_category", unique = true)
    private String itemCategoryString;

    @Override
    public String toString() {
        return idItemCategory.toString();
    }
}
