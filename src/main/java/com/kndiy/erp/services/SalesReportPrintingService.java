package com.kndiy.erp.services;

import com.kndiy.erp.dto.SaleDeliveryDto;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.services.sales.SaleLotService;
import com.sun.source.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


@Service
public class SalesReportPrintingService {

    @Autowired
    private SaleLotService saleLotService;

    public TreeSet<SaleDeliveryDto> findAllDeliveryDtoFromDateToDate(LocalDate fromDate, LocalDate toDate) {

        Comparator<SaleDeliveryDto> comparator = ((d1, d2) -> {
            if (d1.getDeliveryDate().equals(d2.getDeliveryDate()) && d1.getDepartFrom().equals(d2.getDepartFrom()) && d1.getTurnNumber().equals(d2.getTurnNumber())) {
                return d1.getLotName().compareTo(d2.getLotName());
            }
            else if (d1.getDeliveryDate().equals(d2.getDeliveryDate()) && d1.getDepartFrom().equals(d2.getDepartFrom())) {
                return d1.getTurnNumber().compareTo(d2.getTurnNumber());
            }
            else if (d1.getDeliveryDate().equals(d2.getDeliveryDate())) {
                return d1.getDepartFrom().compareTo(d2.getDepartFrom());
            }
            return d2.getDeliveryDate().compareTo(d1.getDeliveryDate());
        });

        TreeSet<SaleDeliveryDto> deliveryDtoSet = new TreeSet<>(comparator);

        List<SaleLot> saleLotList = saleLotService.findAllByDeliveryDate(fromDate, toDate);

        for (SaleLot lot : saleLotList) {
            if (lot.getDeliveryDate() == null || lot.getFromAddress() == null || lot.getInventoryOutList() == null || lot.getInventoryOutList().isEmpty()) {
                continue;
            }

            deliveryDtoSet.add(new SaleDeliveryDto(lot.getDeliveryDate(),
                    lot.getFromAddress().getAddressName(),
                    lot.getDeliveryTurn(),
                    lot.getToAddress().getAddressName(),
                    lot.getSaleContainer().getSaleArticle().getSale().getIdSale(),
                    lot.getSaleContainer().getSaleArticle().getSale().getOrderName(),
                    lot.getSaleContainer().getSaleArticle().getIdSaleArticle(),
                    lot.getSaleContainer().getSaleArticle().getItemCode().getItemType().getItemTypeString(),
                    lot.getSaleContainer().getSaleArticle().getItemCode().getItemCodeString(),
                    lot.getSaleContainer().getIdSaleContainer(),
                    lot.getSaleContainer().getContainer(),
                    lot.getIdSaleLot(),
                    lot.getLotName()));
        }

        return deliveryDtoSet;
    }
}
