package com.kndiy.erp.entities.salesCluster;

import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_sale")
    private Integer idSale;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company_source", nullable = false)
    private Company companySource;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company_customer")
    private Company customer;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "order_batch")
    private String orderBatch;

    @JoinColumn(name = "id_contact_placer")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contact orderPlacer;

    @OneToMany(mappedBy = "sale")
    private List<SaleArticle> saleArticleList;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "note")
    private String note;

    @Column(name = "done_delivery")
    private Boolean doneDelivery;
    private LocalDateTime createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return idSale.toString();
    }
}
