package com.kndiy.erp.services;

import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliverySummaryDtoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;


@Service
public class SalesReportPrintingService {

    @Autowired
    private SaleLotService saleLotService;

    public SaleDeliveryDtoWrapper makeSaleDeliveryDtoWrapperFromDateToDate(LocalDate fromDate, LocalDate toDate) {

        Comparator<SaleDeliveryDto> comparator = ((d1, d2) -> {

            if (!d1.getDeliveryDate().equals(d2.getDeliveryDate())) {
                return d2.getDeliveryDate().compareTo(d1.getDeliveryDate());
            }
            else if (!d1.getDepartFrom().equals(d2.getDepartFrom())) {
                return d1.getDepartFrom().compareTo(d2.getDepartFrom());
            }
            else if (!d1.getDeliveryTurn().equals(d2.getDeliveryTurn())) {
                return d1.getDeliveryTurn().compareTo(d2.getDeliveryTurn());
            }
            else if (!d1.getDeliverTo().equals(d2.getDeliverTo())) {
                return d1.getDeliverTo().compareTo(d2.getDeliverTo());
            }
            else if (!d1.getItemType().equals(d2.getItemType())) {
                return d1.getItemType().compareTo(d2.getItemType());
            }
            else if (!d1.getOrderName().equals(d2.getOrderName())) {
                return d1.getOrderName().compareTo(d2.getOrderName());
            }
            else if (!d1.getItemCode().equals(d2.getItemCode())) {
                return d1.getItemCode().compareTo(d2.getItemCode());
            }
            else if (!d1.getContainer().equals(d2.getContainer())) {
                return d1.getContainer().compareTo(d2.getContainer());
            }

            return d1.getLotName().compareTo(d2.getLotName());
        });

        LinkedList<SaleDeliveryDto> deliveryDtoList = new LinkedList<>();

        List<SaleLot> saleLotList = saleLotService.findAllByDeliveryDate(fromDate, toDate);

        for (SaleLot lot : saleLotList) {
            if (lot.getDeliveryDate() == null || lot.getFromAddress() == null || lot.getInventoryOutList() == null || lot.getInventoryOutList().isEmpty()) {
                continue;
            }

            SaleDeliveryDto saleDeliveryDto = mapSaleDeliveryDtoFromSaleLot(lot);

            deliveryDtoList.add(saleDeliveryDto);
        }

        deliveryDtoList.sort(comparator);

        SaleDeliveryDtoWrapper saleDeliveryDtoWrapper = new SaleDeliveryDtoWrapper();
        saleDeliveryDtoWrapper.setSaleDeliveryDtoList(deliveryDtoList);

        return saleDeliveryDtoWrapper;
    }

    private static SaleDeliveryDto mapSaleDeliveryDtoFromSaleLot(SaleLot lot) {
        SaleDeliveryDto saleDeliveryDto = new SaleDeliveryDto();
        saleDeliveryDto.setDeliveryDate(lot.getDeliveryDate());
        saleDeliveryDto.setDepartFrom(lot.getFromAddress().getAddressName());
        saleDeliveryDto.setDeliveryTurn(lot.getDeliveryTurn());
        saleDeliveryDto.setDeliverTo(lot.getToAddress().getAddressName());
        saleDeliveryDto.setIdSale(lot.getSaleContainer().getSaleArticle().getSale().getIdSale());
        saleDeliveryDto.setOrderName(lot.getSaleContainer().getSaleArticle().getSale().getOrderName());
        saleDeliveryDto.setIdSaleArticle(lot.getSaleContainer().getSaleArticle().getIdSaleArticle());
        saleDeliveryDto.setItemType(lot.getSaleContainer().getSaleArticle().getItemCode().getItemType().getItemTypeString());
        saleDeliveryDto.setItemCode(lot.getSaleContainer().getSaleArticle().getItemCode().getItemCodeString());
        saleDeliveryDto.setIdSaleContainer(lot.getSaleContainer().getIdSaleContainer());
        saleDeliveryDto.setContainer(lot.getSaleContainer().getContainer());
        saleDeliveryDto.setIdSaleLot(lot.getIdSaleLot());
        saleDeliveryDto.setLotName(lot.getLotName());
        return saleDeliveryDto;
    }

    public void serveDeliveryNote(OutputStream outputStream, SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws MismatchedUnitException {

        TreeMap<List<String>, SaleDeliveryDtoWrapper> detailMap = mapDetailDeliveryData(saleDeliveryDtoWrapper);

        TreeMap<String, SaleDeliverySummaryDtoWrapper> summaryMap = mapSummaryData(detailMap);

        System.out.println(summaryMap);
        System.out.println();
        System.out.println(detailMap);
    }

    private TreeMap<String, SaleDeliverySummaryDtoWrapper> mapSummaryData(TreeMap<List<String>, SaleDeliveryDtoWrapper> detailMap) throws MismatchedUnitException {

        TreeMap<String, SaleDeliverySummaryDtoWrapper> summaryMap = new TreeMap<>();

        for (Map.Entry<List<String>, SaleDeliveryDtoWrapper> entry : detailMap.entrySet()) {

            SaleDeliveryDtoWrapper saleDeliveryDtoWrapper = entry.getValue();

            List<String> key = entry.getKey();
            String orderName = key.get(1);
            String itemCodeString = key.get(2);
            String container = key.get(3);
            String itemTypeString = key.get(0);

            String containerQuantity = saleDeliveryDtoWrapper.getContainerQuantity();
            String containerEquivalent = saleDeliveryDtoWrapper.getContainerEquivalent();
            Integer containerRolls = saleDeliveryDtoWrapper.getContainerRolls();

            SaleDeliverySummaryDto saleDeliverySummaryDto = new SaleDeliverySummaryDto();
            saleDeliverySummaryDto.setOrderName(orderName);
            saleDeliverySummaryDto.setItemCodeString(itemCodeString);
            saleDeliverySummaryDto.setContainer(container);
            saleDeliverySummaryDto.setContainerQuantity(containerQuantity);
            saleDeliverySummaryDto.setContainerEquivalent(containerEquivalent);
            saleDeliverySummaryDto.setContainerRolls(containerRolls);

            SaleDeliverySummaryDtoWrapper saleDeliverySummaryDtoWrapper = summaryMap.get(itemTypeString);
            if (saleDeliverySummaryDtoWrapper == null) {
                saleDeliverySummaryDtoWrapper = new SaleDeliverySummaryDtoWrapper();
                saleDeliverySummaryDtoWrapper.setSaleDeliverySummaryDtoTreeSet(new TreeSet<>(List.of(saleDeliverySummaryDto)));
                summaryMap.put(itemTypeString, saleDeliverySummaryDtoWrapper);
            }
            else {
                saleDeliverySummaryDtoWrapper.getSaleDeliverySummaryDtoTreeSet().add(saleDeliverySummaryDto);
            }

            if (saleDeliverySummaryDtoWrapper.getDeliveryQuantity() == null) {
                saleDeliverySummaryDtoWrapper.setDeliveryQuantity(containerQuantity);
            }
            else {
                saleDeliverySummaryDtoWrapper.setDeliveryQuantity(new Quantity(saleDeliverySummaryDtoWrapper.getDeliveryQuantity()).plus(new Quantity(containerQuantity)).toString());
            }

            if (saleDeliverySummaryDtoWrapper.getDeliveryEquivalent() == null) {
                saleDeliverySummaryDtoWrapper.setDeliveryEquivalent(containerEquivalent);
            }
            else {
                saleDeliverySummaryDtoWrapper.setDeliveryEquivalent(new Quantity(saleDeliverySummaryDtoWrapper.getDeliveryEquivalent()).plus(new Quantity(containerEquivalent)).toString());
            }

            if (saleDeliverySummaryDtoWrapper.getDeliveryRolls() == null) {
                saleDeliverySummaryDtoWrapper.setDeliveryRolls(containerRolls);
            }
            else {
                saleDeliverySummaryDtoWrapper.setDeliveryRolls(saleDeliverySummaryDtoWrapper.getDeliveryRolls() + containerRolls);
            }
        }

        return summaryMap;
    }

    private TreeMap<List<String>, SaleDeliveryDtoWrapper> mapDetailDeliveryData(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws MismatchedUnitException {

        TreeMap<List<String>, SaleDeliveryDtoWrapper> detailMap = setupDetailDeliveryTreeMap();

        LocalDate deliveryDate = saleDeliveryDtoWrapper.getDeliveryDate();
        Integer deliveryTurn = saleDeliveryDtoWrapper.getDeliveryTurn();

        for (SaleDeliveryDto saleDeliveryDto : saleDeliveryDtoWrapper.getSaleDeliveryDtoList()) {

            if (!saleDeliveryDto.getDeliveryTurn().equals(deliveryTurn) && !saleDeliveryDto.getDeliveryDate().equals(deliveryDate)) {
                continue;
            }

            String orderName = saleDeliveryDto.getOrderName();
            String itemCodeString = saleDeliveryDto.getItemCode();
            String container = saleDeliveryDto.getContainer();
            String itemTypeString = saleDeliveryDto.getItemType();
            List<String> key = new ArrayList<>(List.of(itemTypeString, orderName, itemCodeString, container));

            SaleLot saleLot = saleLotService.findByIdSaleLot(saleDeliveryDto.getIdSaleLot());
            if (saleLot == null) {
                continue;
            }

            List<InventoryOut> inventoryOutList = saleLot.getInventoryOutList();
            if (inventoryOutList == null || inventoryOutList.isEmpty()) {
                continue;
            }

            Quantity lotQuantity = null;
            Quantity lotEquivalent = null;
            Integer lotRolls = inventoryOutList.size();

            List<InventoryOutDto> inventoryOutDtoList = new ArrayList<>();
            for (InventoryOut inventoryOut : inventoryOutList) {
                InventoryOutDto inventoryOutDto = new InventoryOutDto();
                inventoryOutDto.setQuantity(inventoryOut.getQuantity());
                inventoryOutDto.setEquivalent(inventoryOut.getEquivalent());
                inventoryOutDto.setIdInventoryOut(inventoryOut.getIdInventoryOut());

                if (lotQuantity == null) lotQuantity = new Quantity(inventoryOutDto.getQuantity());
                else lotQuantity = lotQuantity.plus(new Quantity(inventoryOutDto.getQuantity()));

                if (lotEquivalent == null) lotEquivalent = new Quantity(inventoryOutDto.getEquivalent());
                else lotEquivalent = lotEquivalent.plus(new Quantity(inventoryOutDto.getEquivalent()));

                inventoryOutDtoList.add(inventoryOutDto);
            }

            saleDeliveryDto.setInventoryOutDtoList(new TreeSet<>(inventoryOutDtoList));
            saleDeliveryDto.setLotQuantity(lotQuantity.toString());
            saleDeliveryDto.setLotEquivalent(lotEquivalent.toString());
            saleDeliveryDto.setLotRolls(lotRolls);

            SaleDeliveryDtoWrapper outputDeliveryDtoWrapper = detailMap.get(key);
            if (outputDeliveryDtoWrapper == null) {
                outputDeliveryDtoWrapper = new SaleDeliveryDtoWrapper();
                outputDeliveryDtoWrapper.setSaleDeliveryDtoList(List.of(saleDeliveryDto));
                detailMap.put(key, outputDeliveryDtoWrapper);
            }
            else {
                outputDeliveryDtoWrapper.getSaleDeliveryDtoList().add(saleDeliveryDto);
            }

            if (outputDeliveryDtoWrapper.getContainerQuantity() == null) {
                outputDeliveryDtoWrapper.setContainerQuantity(lotQuantity.toString());
            }
            else {
                outputDeliveryDtoWrapper.setContainerQuantity(new Quantity(saleDeliveryDtoWrapper.getContainerQuantity()).plus(lotQuantity).toString());
            }

            if (outputDeliveryDtoWrapper.getContainerEquivalent() == null) {
                outputDeliveryDtoWrapper.setContainerEquivalent(lotEquivalent.toString());
            }
            else {
                outputDeliveryDtoWrapper.setContainerEquivalent(new Quantity(saleDeliveryDtoWrapper.getContainerQuantity()).plus(lotEquivalent).toString());
            }

            if (outputDeliveryDtoWrapper.getContainerRolls() == null) {
                outputDeliveryDtoWrapper.setContainerRolls(lotRolls);
            }
            else {
                outputDeliveryDtoWrapper.setContainerRolls(outputDeliveryDtoWrapper.getContainerRolls() + lotRolls);
            }

        }
        return detailMap;
    }

    private static TreeMap<List<String>, SaleDeliveryDtoWrapper> setupDetailDeliveryTreeMap() {
        Comparator<List<String>> comparator = (l1, l2) -> {
            if (!l1.get(0).equals(l2.get(0))) {
                return l1.get(0).compareTo(l2.get(0));
            }
            else if (!l1.get(1).equals(l2.get(1))) {
                return l1.get(1).compareTo(l2.get(1));
            }
            else if (!l1.get(2).equals(l2.get(2))) {
                return l1.get(2).compareTo(l2.get(2));
            }
            return l1.get(3).compareTo(l2.get(3));
        };

        return new TreeMap<>(comparator);
    }

}
