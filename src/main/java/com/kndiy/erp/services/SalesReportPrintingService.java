package com.kndiy.erp.services;

import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryHeaderDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entities.itemCodeCluster.ItemSellPrice;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.pdfExpoter.AccountSettlingNotePdfCreator;
import com.kndiy.erp.pdfExpoter.DeliveryLabelPdfCreator;
import com.kndiy.erp.pdfExpoter.DeliveryNotePdfCreator;
import com.kndiy.erp.services.item.ItemCodeSupplierService;
import com.kndiy.erp.services.sales.SaleContainerService;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoContainerWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoItemTypeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Service
public class SalesReportPrintingService {

    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private SaleContainerService saleContainerService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;

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
        saleDeliveryDto.setOrderBatch(lot.getSaleContainer().getSaleArticle().getSale().getOrderBatch());
        saleDeliveryDto.setIdSaleArticle(lot.getSaleContainer().getSaleArticle().getIdSaleArticle());
        saleDeliveryDto.setItemType(lot.getSaleContainer().getSaleArticle().getItemCode().getItemType().getItemTypeString());
        saleDeliveryDto.setItemCode(lot.getSaleContainer().getSaleArticle().getItemCode().getItemCodeString());
        saleDeliveryDto.setIdSaleContainer(lot.getSaleContainer().getIdSaleContainer());
        saleDeliveryDto.setContainer(lot.getSaleContainer().getContainer());
        saleDeliveryDto.setLotName(lot.getLotName());
        saleDeliveryDto.setSaleSource(lot.getSaleContainer().getSaleArticle().getSale().getCompanySource().getAbbreviation());
        saleDeliveryDto.setCustomer(lot.getSaleContainer().getSaleArticle().getSale().getCustomer().getAbbreviation());
        saleDeliveryDto.setIdSaleLot(lot.getIdSaleLot());

        return saleDeliveryDto;
    }

    public void serveNote(OutputStream outputStream, SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws MismatchedUnitException, IOException {

        TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap = mapSaleContainerData(saleDeliveryDtoWrapper);
        TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap = mapItemTypeData(containerMap);

        SaleDeliveryHeaderDto saleDeliveryHeaderDto = makeSaleDeliveryHeaderDto(saleDeliveryDtoWrapper, itemTypeMap);

        String reportName = saleDeliveryDtoWrapper.getReportName();

        byte[] fileBytes = null;

        switch (reportName) {
            case "delivery-note" -> {
                DeliveryNotePdfCreator deliveryNotePdfCreator = new DeliveryNotePdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = deliveryNotePdfCreator.create();
            }
            case "delivery-label" -> {
                DeliveryLabelPdfCreator deliveryLabelPdfCreator = new DeliveryLabelPdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = deliveryLabelPdfCreator.create();
            }
            case "account-settling-note" -> {
                AccountSettlingNotePdfCreator accountSettlingNotePdfCreator = new AccountSettlingNotePdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = accountSettlingNotePdfCreator.create();
            }
            case "both" -> {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
                String pdfName = dtf.format(saleDeliveryHeaderDto.getDeliveryDate()) + "_Turn" + saleDeliveryHeaderDto.getDeliveryTurn() + "_";

                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                DeliveryNotePdfCreator note = new DeliveryNotePdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                DeliveryLabelPdfCreator labels = new DeliveryLabelPdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);

                ZipEntry entry = new ZipEntry(pdfName + "DeliveryNote.pdf");
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(note.create());
                zipOutputStream.closeEntry();

                entry = new ZipEntry(pdfName + "DeliveryLabels.pdf");
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(labels.create());
                zipOutputStream.closeEntry();

                zipOutputStream.close();
            }
        }

        if (fileBytes != null) {
            printFinalPdf(fileBytes, outputStream);
        }
    }

    public byte[] serveNoteBytes(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws MismatchedUnitException, IOException {

        TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap = mapSaleContainerData(saleDeliveryDtoWrapper);
        TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap = mapItemTypeData(containerMap);

        SaleDeliveryHeaderDto saleDeliveryHeaderDto = makeSaleDeliveryHeaderDto(saleDeliveryDtoWrapper, itemTypeMap);

        String reportName = saleDeliveryDtoWrapper.getReportName();

        byte[] fileBytes = null;

        switch (reportName) {
            case "delivery-note" -> {
                DeliveryNotePdfCreator deliveryNotePdfCreator = new DeliveryNotePdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = deliveryNotePdfCreator.create();
            }
            case "delivery-label" -> {
                DeliveryLabelPdfCreator deliveryLabelPdfCreator = new DeliveryLabelPdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = deliveryLabelPdfCreator.create();
            }
            case "account-settling-note" -> {
                AccountSettlingNotePdfCreator accountSettlingNotePdfCreator = new AccountSettlingNotePdfCreator(containerMap, itemTypeMap, saleDeliveryHeaderDto);
                fileBytes = accountSettlingNotePdfCreator.create();
            }
            case "both" -> {
            }
        }

        return fileBytes;
    }

    private void printFinalPdf(byte[] fileBytes, OutputStream responseOutputStream) throws IOException {

        for (byte aByte : fileBytes) {
            responseOutputStream.write(aByte);
        }
    }

    private SaleDeliveryHeaderDto makeSaleDeliveryHeaderDto(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper, TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap) throws MismatchedUnitException {

        SaleDeliveryHeaderDto saleDeliveryHeaderDto = new SaleDeliveryHeaderDto();

        saleDeliveryHeaderDto.setIdSaleLot(saleDeliveryDtoWrapper.getIdSaleLot());
        saleDeliveryHeaderDto.setDeliveryDate(saleDeliveryDtoWrapper.getDeliveryDate());
        saleDeliveryHeaderDto.setDeliveryTurn(saleDeliveryDtoWrapper.getDeliveryTurn());

        Quantity deliveryAmount = new Quantity("0.00 VND");
        float vatRate = saleDeliveryDtoWrapper.getVatRate();

        for (Map.Entry<String, SaleDeliveryDtoItemTypeWrapper> entry : itemTypeMap.entrySet()) {

            SaleDeliveryDtoItemTypeWrapper wrapper = entry.getValue();
            deliveryAmount = deliveryAmount.plus(new Quantity(wrapper.getItemTypeAmount()));
        }

        Quantity vat = deliveryAmount.times(vatRate);
        Quantity deliveryAmountWithVat = deliveryAmount.plus(vat);

        saleDeliveryHeaderDto.setDeliveryAmount(deliveryAmount.toString());
        saleDeliveryHeaderDto.setVat(vat.toString());
        saleDeliveryHeaderDto.setDeliveryAmountWithVat(deliveryAmountWithVat.toString());
        saleDeliveryHeaderDto.setVatRate(String.valueOf(vatRate));

        SaleLot saleLot = saleLotService.findByIdSaleLot(saleDeliveryHeaderDto.getIdSaleLot());

        Company saleSource = saleLot.getSaleContainer().getSaleArticle().getSale().getCompanySource();
        Company customer = saleLot.getSaleContainer().getSaleArticle().getSale().getCustomer();

        Address saleSourceHQ = companyClusterService.findHQAddressByCompanyNameEn(saleSource.getNameEn());
        Address customerHQ = companyClusterService.findHQAddressByCompanyNameEn(customer.getNameEn());

        Contact saleSourceLandLine = companyClusterService.findLandLineContactByHQAddressName(saleSourceHQ.getAddressName());



        Contact receiver = saleLot.getReceiver();

        saleDeliveryHeaderDto.setSaleSourceNameVn(saleSource.getNameVn());
        saleDeliveryHeaderDto.setSaleSourceHQAddress(saleSourceHQ.getAddressVn());

        if (saleSourceLandLine != null) {
            saleDeliveryHeaderDto.setSaleSourceLandLine(saleSourceLandLine.getPhone1());
        }
        else {
            saleDeliveryHeaderDto.setSaleSourceLandLine("Err: Please add a landline!");
        }

        saleDeliveryHeaderDto.setCustomerNameVn(customer.getNameVn());
        saleDeliveryHeaderDto.setCustomerHQAddress(customerHQ.getAddressVn());
        saleDeliveryHeaderDto.setDeliverToAddressName(receiver.getAddress().getAddressName());
        saleDeliveryHeaderDto.setDeliverToAddressVn(receiver.getAddress().getAddressVn());
        saleDeliveryHeaderDto.setReceiverName(receiver.getContactName());
        saleDeliveryHeaderDto.setReceiverPhone(receiver.getPhone1());

        return saleDeliveryHeaderDto;
    }

    private TreeMap<String, SaleDeliveryDtoItemTypeWrapper> mapItemTypeData(TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> lotMap) throws MismatchedUnitException {

        TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap = new TreeMap<>();

        for (Map.Entry<List<String>, SaleDeliveryDtoContainerWrapper> entry : lotMap.entrySet()) {

            SaleDeliveryDtoContainerWrapper saleDeliveryDtoContainerWrapper = entry.getValue();

            List<String> key = entry.getKey();
            String itemTypeString = key.get(0);
            String orderName = key.get(1);
            String orderBatch = key.get(2);
            String itemCodeString = key.get(3);
            String container = key.get(4);

            Quantity containerQuantity = new Quantity(saleDeliveryDtoContainerWrapper.getContainerQuantity(), RoundingMode.DOWN, 2);
            Quantity containerEquivalent = new Quantity(saleDeliveryDtoContainerWrapper.getContainerEquivalent(), RoundingMode.DOWN, 2);
            Integer containerRolls = saleDeliveryDtoContainerWrapper.getContainerRolls();
            Quantity containerEquivalentAdjusted = new Quantity(saleDeliveryDtoContainerWrapper.getContainerEquivalentAdjusted(), RoundingMode.DOWN, 2);
            Quantity containerAmount = new Quantity(saleDeliveryDtoContainerWrapper.getContainerAmount(), RoundingMode.DOWN, 2);

            SaleDeliverySummaryDto saleDeliverySummaryDto = new SaleDeliverySummaryDto();
            saleDeliverySummaryDto.setOrderName(orderName);
            saleDeliverySummaryDto.setOrderBatch(orderBatch);
            saleDeliverySummaryDto.setItemCodeString(itemCodeString);
            saleDeliverySummaryDto.setContainer(container);
            saleDeliverySummaryDto.setContainerQuantity(containerQuantity.toString());
            saleDeliverySummaryDto.setContainerEquivalent(containerEquivalent.toString());
            saleDeliverySummaryDto.setContainerRolls(containerRolls);
            saleDeliverySummaryDto.setContainerAmount(containerAmount.toString());
            saleDeliverySummaryDto.setContainerEquivalentAdjusted(containerEquivalentAdjusted.toString());

            SaleDeliveryDtoItemTypeWrapper saleDeliveryDtoItemTypeWrapper = itemTypeMap.get(itemTypeString);
            if (saleDeliveryDtoItemTypeWrapper == null) {
                saleDeliveryDtoItemTypeWrapper = new SaleDeliveryDtoItemTypeWrapper();
                saleDeliveryDtoItemTypeWrapper.setSaleDeliverySummaryDtoTreeSet(new TreeSet<>(List.of(saleDeliverySummaryDto)));
                itemTypeMap.put(itemTypeString, saleDeliveryDtoItemTypeWrapper);
            }
            else {
                saleDeliveryDtoItemTypeWrapper.getSaleDeliverySummaryDtoTreeSet().add(saleDeliverySummaryDto);
            }

            if (saleDeliveryDtoItemTypeWrapper.getItemTypeQuantity() == null) {
                saleDeliveryDtoItemTypeWrapper.setItemTypeQuantity(containerQuantity.toString());
            }
            else {
                saleDeliveryDtoItemTypeWrapper.setItemTypeQuantity(new Quantity(saleDeliveryDtoItemTypeWrapper.getItemTypeQuantity()).plus(containerQuantity).toString());
            }

            if (saleDeliveryDtoItemTypeWrapper.getItemTypeEquivalent() == null) {
                saleDeliveryDtoItemTypeWrapper.setItemTypeEquivalent(containerEquivalent.toString());
            }
            else {
                saleDeliveryDtoItemTypeWrapper.setItemTypeEquivalent(new Quantity(saleDeliveryDtoItemTypeWrapper.getItemTypeEquivalent()).plus(containerEquivalent).toString());
            }

            if (saleDeliveryDtoItemTypeWrapper.getItemTypeRolls() == null) {
                saleDeliveryDtoItemTypeWrapper.setItemTypeRolls(containerRolls);
            }
            else {
                saleDeliveryDtoItemTypeWrapper.setItemTypeRolls(saleDeliveryDtoItemTypeWrapper.getItemTypeRolls() + containerRolls);
            }

            if (saleDeliveryDtoItemTypeWrapper.getItemTypeAmount() == null) {
                saleDeliveryDtoItemTypeWrapper.setItemTypeAmount(containerAmount.toString());
            }
            else {
                saleDeliveryDtoItemTypeWrapper.setItemTypeAmount(new Quantity(saleDeliveryDtoItemTypeWrapper.getItemTypeAmount()).plus(containerAmount).toString());
            }

            if (saleDeliveryDtoItemTypeWrapper.getItemTypeEquivalentAdjusted() == null) {
                saleDeliveryDtoItemTypeWrapper.setItemTypeEquivalentAdjusted(containerEquivalentAdjusted.toString());
            }
            else {
                saleDeliveryDtoItemTypeWrapper.setItemTypeEquivalentAdjusted(new Quantity(saleDeliveryDtoItemTypeWrapper.getItemTypeEquivalentAdjusted()).plus(containerEquivalentAdjusted).toString());
            }

        }

        return itemTypeMap;
    }

    private static ItemSellPrice getItemSellPrice(SaleLot saleLot) throws IllegalArgumentException {
        ItemCode itemCode = saleLot.getSaleContainer().getSaleArticle().getItemCode();
        TreeSet<ItemSellPrice> itemSellPriceSet = new TreeSet<>(itemCode.getItemSellPriceList());

        //check if itemSellPrice Contract is up-to-date or outdated
        if (itemSellPriceSet.isEmpty()) {
            throw new IllegalArgumentException("Please check ItemSellPrice of " + itemCode.getItemCodeString() + " as no contract was linked with it!");
        }

        ItemSellPrice itemSellPrice = itemSellPriceSet.first();
        LocalDate expiredDate = itemSellPrice.getToDate();

        if (expiredDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Contract: " + itemSellPrice.getItemSellPriceContract() + " of ItemCode: " +itemCode.getItemCodeString() + " has expired, please check!");
        }

        return itemSellPrice;
    }

    private static TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> setupSaleContainerMap() {
        Comparator<List<String>> comparator = (l1, l2) -> {
            //item type
            if (!l1.get(0).equals(l2.get(0))) {
                return l1.get(0).compareTo(l2.get(0));
            }
            //order name
            else if (!l1.get(1).equals(l2.get(1))) {
                return l1.get(1).compareTo(l2.get(1));
            }
            //item code
            else if (!l1.get(3).equals(l2.get(3))) {
                return l1.get(3).compareTo(l2.get(3));
            }
            //container
            return l1.get(4).compareTo(l2.get(4));
        };

        return new TreeMap<>(comparator);
    }


    private TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> mapSaleContainerData(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws MismatchedUnitException {

        TreeSet<Integer> idSaleContainerSet = makeIdContainerSet(saleDeliveryDtoWrapper);
        TreeMap<Integer, List<String>> containerAdjustmentDataMap = mapContainerAdjustmentData(idSaleContainerSet);

        TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap = setupSaleContainerMap();
        fillSaleContainerMap(containerMap, saleDeliveryDtoWrapper, containerAdjustmentDataMap);

        return containerMap;
    }

    private void fillSaleContainerMap(TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap, SaleDeliveryDtoWrapper saleDeliveryDtoWrapper, TreeMap<Integer, List<String>> containerAdjustmentDataMap) throws MismatchedUnitException {

        for (SaleDeliveryDto saleDeliveryDto : saleDeliveryDtoWrapper.getSaleDeliveryDtoList()) {

            //Created key
            String itemTypeString = saleDeliveryDto.getItemType();
            String orderName = saleDeliveryDto.getOrderName();
            String orderBatch = saleDeliveryDto.getOrderBatch();
            String itemCodeString = saleDeliveryDto.getItemCode();
            String container = saleDeliveryDto.getContainer();
            List<String> key = new ArrayList<>(List.of(itemTypeString, orderName, orderBatch, itemCodeString, container));

            //if lot was deleted by other user, skip
            SaleLot saleLot = saleLotService.findByIdSaleLot(saleDeliveryDto.getIdSaleLot());
            if (saleLot == null) {
                continue;
            }

            //if Lot was created but is currently containing no inventory Out data, skip it
            List<String> containerAdjustmentData = containerAdjustmentDataMap.get(saleLot.getSaleContainer().getIdSaleContainer());
            if (containerAdjustmentData == null) {
                continue;
            }

            List<InventoryOut> inventoryOutList = saleLot.getInventoryOutList();
            if (inventoryOutList == null || inventoryOutList.isEmpty()) {
                continue;
            }

            //find corresponding itemCodeSupplierString
            String supplierNameEn = saleLot.getSupplier().getNameEn();
            ItemCodeSupplier itemCodeSupplier = itemCodeSupplierService.findByItemCodeStringAndSupplierNameEn(itemCodeString, supplierNameEn);
            saleDeliveryDto.setLotItemCodeSupplierString(itemCodeSupplier.getItemCodeSupplierString());
            saleDeliveryDto.setSupplierAbbreviation(saleLot.getSupplier().getAbbreviation());

            //start by calculating lot Quantity by iterating inventoriesOut
            Quantity lotQuantity = null;
            Quantity lotEquivalent = null;
            Integer lotRolls = inventoryOutList.size();

            List<InventoryOutDto> inventoryOutDtoList = new ArrayList<>();
            for (InventoryOut inventoryOut : inventoryOutList) {
                InventoryOutDto inventoryOutDto = new InventoryOutDto();
                inventoryOutDto.setQuantity(new Quantity(inventoryOut.getQuantity(), RoundingMode.DOWN, 2).toString());
                inventoryOutDto.setEquivalent(new Quantity(inventoryOut.getEquivalent(), RoundingMode.DOWN, 2).toString());
                inventoryOutDto.setIdInventoryOut(inventoryOut.getIdInventoryOut());
                inventoryOutDto.setProductionCode(inventoryOut.getInventory().getProductionCode());

                if (lotQuantity == null) lotQuantity = new Quantity(inventoryOutDto.getQuantity(), RoundingMode.DOWN, 2);
                else lotQuantity = lotQuantity.plus(new Quantity(inventoryOutDto.getQuantity()));

                if (lotEquivalent == null) lotEquivalent = new Quantity(inventoryOutDto.getEquivalent(), RoundingMode.DOWN, 2);
                else lotEquivalent = lotEquivalent.plus(new Quantity(inventoryOutDto.getEquivalent()));

                inventoryOutDtoList.add(inventoryOutDto);
            }

            //after having lot equivalent (quantity in units that customer ordered), look for selling contract and take corresponding sell price
            ItemSellPrice itemSellPrice = getItemSellPrice(saleLot);
            Quantity sellRate = new Quantity(itemSellPrice.getItemSellPriceAmount(), itemSellPrice.getItemSellPriceUnit(), RoundingMode.DOWN, 2);

            saleDeliveryDto.setInventoryOutDtoTreeSet(new TreeSet<>(inventoryOutDtoList));
            saleDeliveryDto.setLotQuantity(lotQuantity == null ? "0" : lotQuantity.toString());
            saleDeliveryDto.setLotEquivalent(lotEquivalent == null ? "0" : lotEquivalent.toString());
            saleDeliveryDto.setLotRolls(lotRolls);
            saleDeliveryDto.setLotColor(saleLot.getOrderColor());
            saleDeliveryDto.setLotStyle(saleLot.getOrderStyle());
            saleDeliveryDto.setLotName(saleLot.getLotName());
            saleDeliveryDto.setItemSellPrice(sellRate.toString());
            saleDeliveryDto.setLotNote(saleLot.getNote());
            saleDeliveryDto.setOrderBatch(saleLot.getSaleContainer().getSaleArticle().getSale().getOrderBatch());

            //calculate lotEquivalentAdjusted
            Quantity lotEquivalentAdjusted = calculateLotEquivalentAdjusted(lotEquivalent, containerAdjustmentDataMap, saleLot.getSaleContainer().getIdSaleContainer());
            saleDeliveryDto.setLotEquivalentAdjusted(lotEquivalentAdjusted.toString());
            Quantity lotAmount = lotEquivalentAdjusted.times(sellRate);
            saleDeliveryDto.setLotAmount(lotAmount.toString());

            //Calculate container values
            //Calculate container values
            //Calculate container values
            SaleDeliveryDtoContainerWrapper saleDeliveryDtoContainerWrapper = containerMap.get(key);
            if (saleDeliveryDtoContainerWrapper == null) {
                saleDeliveryDtoContainerWrapper = new SaleDeliveryDtoContainerWrapper();
                saleDeliveryDtoContainerWrapper.setSaleDeliveryDtoList(new LinkedList<>(List.of(saleDeliveryDto)));
                containerMap.put(key, saleDeliveryDtoContainerWrapper);
            }
            else {
                saleDeliveryDtoContainerWrapper.getSaleDeliveryDtoList().add(saleDeliveryDto);
            }

            if (saleDeliveryDtoContainerWrapper.getContainerQuantity() == null) {
                saleDeliveryDtoContainerWrapper.setContainerQuantity(lotQuantity.toString());
            }
            else {
                saleDeliveryDtoContainerWrapper.setContainerQuantity(new Quantity(saleDeliveryDtoContainerWrapper.getContainerQuantity()).plus(lotQuantity).toString());
            }

            if (saleDeliveryDtoContainerWrapper.getContainerEquivalent() == null) {
                saleDeliveryDtoContainerWrapper.setContainerEquivalent(lotEquivalent.toString());
            }
            else {
                saleDeliveryDtoContainerWrapper.setContainerEquivalent(new Quantity(saleDeliveryDtoContainerWrapper.getContainerEquivalent()).plus(lotEquivalent).toString());
            }

            if (saleDeliveryDtoContainerWrapper.getContainerRolls() == null) {
                saleDeliveryDtoContainerWrapper.setContainerRolls(lotRolls);
            }
            else {
                saleDeliveryDtoContainerWrapper.setContainerRolls(saleDeliveryDtoContainerWrapper.getContainerRolls() + lotRolls);
            }

            if (saleDeliveryDtoContainerWrapper.getContainerEquivalentAdjusted() == null) {
                saleDeliveryDtoContainerWrapper.setContainerEquivalentAdjusted(lotEquivalentAdjusted.toString());
            }
            else {
                saleDeliveryDtoContainerWrapper.setContainerEquivalentAdjusted(new Quantity(saleDeliveryDtoContainerWrapper.getContainerEquivalentAdjusted()).plus(lotEquivalentAdjusted).toString());
            }

            if (saleDeliveryDtoContainerWrapper.getContainerAmount() == null) {
                saleDeliveryDtoContainerWrapper.setContainerAmount(lotAmount.toString());
            }
            else {
                saleDeliveryDtoContainerWrapper.setContainerAmount(new Quantity(saleDeliveryDtoContainerWrapper.getContainerAmount()).plus(lotAmount).toString());
            }
        }
    }

    private Quantity calculateLotEquivalentAdjusted(Quantity lotEquivalent, Map<Integer, List<String>> containerAdjustmentDataMap, Integer idSaleContainer) {

        List<String> containerAdjustmentData = containerAdjustmentDataMap.get(idSaleContainer);

        try {

            Quantity containerAllow = new Quantity(containerAdjustmentData.get(0), RoundingMode.DOWN, 2);
            Quantity containerEquivalent = new Quantity(containerAdjustmentData.get(1), RoundingMode.DOWN, 2);
            Quantity differential = containerEquivalent.minus(containerAllow);

            if (differential.getQuantityValue().compareTo(BigDecimal.ZERO) <= 0) {
                return lotEquivalent;
            }

            if (lotEquivalent.compareTo(differential) < 0) {
                containerEquivalent = containerEquivalent.minus(lotEquivalent);
                containerAdjustmentDataMap.put(idSaleContainer, List.of(containerAllow.toString(), containerEquivalent.toString()));
                return lotEquivalent.minus(lotEquivalent);
            }

            containerEquivalent = containerEquivalent.minus(differential);
            containerAdjustmentDataMap.put(idSaleContainer, List.of(containerAllow.toString(), containerEquivalent.toString()));

            return lotEquivalent.minus(differential);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Mismatched units when trying to calculate differential between ContainerAllow and ContainerEquivalent!");
        }
    }

    private TreeMap<Integer, List<String>> mapContainerAdjustmentData(TreeSet<Integer> idContainerSet) throws MismatchedUnitException {

        TreeMap<Integer, List<String>> saleContainerAdjustmentMap = new TreeMap<>();

        for (Integer i : idContainerSet) {

            SaleContainer saleContainer = saleContainerService.findByIdSaleContainer(i);
            if (saleContainer == null) {
                continue;
            }

            Quantity containerOrder = null;
            Quantity containerEquivalent = null;

            List<SaleLot> saleLotList = saleContainer.getSaleLotList();
            if (saleLotList == null || saleLotList.isEmpty()) {
                continue;
            }

            for (SaleLot saleLot : saleLotList) {

                Quantity lotOrder = new Quantity(saleLot.getOrderQuantity(), RoundingMode.DOWN, 2);
                Quantity lotEquivalent = null;

                List<InventoryOut> inventoryOutList = saleLot.getInventoryOutList();
                if (inventoryOutList == null || inventoryOutList.isEmpty()) {
                    continue;
                }

                for (InventoryOut inventoryOut : inventoryOutList) {

                    Quantity equivalent = new Quantity(inventoryOut.getEquivalent(), RoundingMode.DOWN, 2);
                    if (lotEquivalent == null) {
                        lotEquivalent = equivalent;
                    }
                    else {
                        lotEquivalent = lotEquivalent.plus(equivalent);
                    }
                }

                if (containerOrder == null) {
                    containerOrder = lotOrder;
                }
                else {
                    containerOrder = containerOrder.plus(lotOrder);
                }

                if (containerEquivalent == null) {
                    containerEquivalent = lotEquivalent;
                }
                else {
                    containerEquivalent = containerEquivalent.plus(lotEquivalent);
                }
            }

            if (containerOrder != null && containerEquivalent != null) {

                float allowedSurplus = getAllowedSurplus(saleContainer) + 1;
                Quantity containerAllow = containerOrder.times(allowedSurplus);

                List<String> adjustmentData = List.of(containerAllow.toString(), containerEquivalent.toString());
                saleContainerAdjustmentMap.put(i, adjustmentData);
            }
        }

        return saleContainerAdjustmentMap;
    }

    private float getAllowedSurplus(SaleContainer saleContainer) {
        float allowedSurplus;
        try {
            allowedSurplus = saleContainer.getSaleArticle().getAllowedSurplus();
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("AllowSurplus field of Order: "
                    + saleContainer.getSaleArticle().getSale().getOrderName() + " at Article itemCode: "
                    + saleContainer.getSaleArticle().getItemCode().getItemCodeString() + " was not filled! Please check!");
        }
        return allowedSurplus;
    }

    private TreeSet<Integer> makeIdContainerSet(SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) {

        String reportName = saleDeliveryDtoWrapper.getReportName();

        TreeSet<Integer> set = new TreeSet<>();

        LocalDate deliveryDate = saleDeliveryDtoWrapper.getDeliveryDate();
        Integer deliveryTurn = saleDeliveryDtoWrapper.getDeliveryTurn();
        String saleSource = saleDeliveryDtoWrapper.getSaleSource();
        String customer = saleDeliveryDtoWrapper.getCustomer();

        ListIterator<SaleDeliveryDto> iterator = saleDeliveryDtoWrapper.getSaleDeliveryDtoList().listIterator();

        while (iterator.hasNext()) {

            SaleDeliveryDto saleDeliveryDto = iterator.next();

            if ((reportName.equals("delivery-note") || reportName.equals("delivery-label") || reportName.equals("both")) &&
                    (!saleDeliveryDto.getSaleSource().equals(saleSource) ||
                    !saleDeliveryDto.getCustomer().equals(customer) ||
                    !saleDeliveryDto.getDeliveryDate().equals(deliveryDate) ||
                    !saleDeliveryDto.getDeliveryTurn().equals(deliveryTurn))) {

                    iterator.remove();
                    continue;
            }
            else if (reportName.equals("account-settling-note") &&
                    (!saleDeliveryDto.getCustomer().equals(customer) ||
                    !saleDeliveryDto.getDeliveryDate().equals(deliveryDate) ||
                    !saleDeliveryDto.getSaleSource().equals(saleSource))) {

                    iterator.remove();
                    continue;
            }

            Integer idSaleContainer = saleDeliveryDto.getIdSaleContainer();
            if (idSaleContainer != null) {
                set.add(idSaleContainer);
            }
        }

        return set;
    }
}
