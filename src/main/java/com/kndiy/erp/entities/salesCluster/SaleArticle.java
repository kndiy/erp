package com.kndiy.erp.entities.salesCluster;

import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale_articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_sale_article")
    private Integer idSaleArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sale")
    private Sale sale;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_code", nullable = false)
    private ItemCode itemCode;

    @OneToMany(mappedBy = "saleArticle")
    private List<SaleContainer> saleContainerList;

    @Column(name = "allowed_surplus")
    private Float allowedSurplus = 0.03F;

    @Column(name = "request_delivery_date")
    private LocalDate requestDeliveryDate;

    private LocalDateTime createdAt;
    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return idSaleArticle.toString();
    }
}
