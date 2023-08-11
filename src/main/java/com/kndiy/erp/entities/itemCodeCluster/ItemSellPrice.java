package com.kndiy.erp.entities.itemCodeCluster;

import com.kndiy.erp.entities.companyCluster.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "item_sell_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSellPrice implements Serializable, Comparable<ItemSellPrice> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_item_sell_price")
    private Integer idItemSellPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_company")
    private Company customer;
    @Column(name = "from_date")
    private LocalDate fromDate;
    @Column(name = "to_date")
    private LocalDate toDate;
    @Column(name = "item_sell_price")
    private Float itemSellPriceAmount;
    @Column(name = "note")
    private String note;
    @Column(name = "sell_price_unit")
    private String itemSellPriceUnit;
    @Column(name = "contract")
    private String itemSellPriceContract;
    @ManyToMany(mappedBy = "itemSellPriceList")
    List<ItemCode> itemCodeList;

    @Override
    public int compareTo(ItemSellPrice isp) {
        return isp.itemSellPriceContract.compareTo(this.itemSellPriceContract);
    }

    @Override
    public String toString() {
        return idItemSellPrice.toString();
    }
}
