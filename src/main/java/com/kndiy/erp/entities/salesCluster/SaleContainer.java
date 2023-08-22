package com.kndiy.erp.entities.salesCluster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "sale_containers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_sale_container")
    private Integer idSaleContainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sale_article")
    private SaleArticle saleArticle;

    @OneToMany(mappedBy = "saleContainer")
    private List<SaleLot> saleLotList;

    @Column(name = "sale_container")
    private String container;

    @Column(name = "order_unit")
    private String orderUnit;

    @Column(name = "for_claim")
    private Boolean forClaim;

    @Override
    public String toString() {
        return idSaleContainer.toString();
    }
}
