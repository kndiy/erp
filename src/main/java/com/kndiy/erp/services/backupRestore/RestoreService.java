package com.kndiy.erp.services.backupRestore;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.salesCluster.Sale;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.inventory.InventoryInService;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.services.inventory.InventoryService;
import com.kndiy.erp.services.sales.SaleArticleService;
import com.kndiy.erp.services.sales.SaleContainerService;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.services.sales.SaleService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kndiy.erp.dto.*;
import com.kndiy.erp.entities.itemCodeCluster.*;
import com.kndiy.erp.services.item.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class RestoreService {
    private JsonObject root;
    private List<String> results;
    private DateTimeFormatter dtf;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private ItemCategoryService itemCategoryService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private ItemSellPriceService itemSellPriceService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;
    @Autowired
    private ItemCodeSupplierEquivalentService itemCodeSupplierEquivalentService;
    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleArticleService saleArticleService;
    @Autowired
    private SaleContainerService saleContainerService;
    @Autowired
    private SaleLotService saleLotService;
    public String restore(MultipartFile backupFile) throws IOException, MismatchedUnitException {

        root = (JsonObject) JsonParser.parseReader(new InputStreamReader(backupFile.getInputStream()));
        results = new ArrayList<>();
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        restoreEntity("Company");
        log.info(results.toString());
        results.clear();
        restoreEntity("Address");
        log.info(results.toString());
        results.clear();
        restoreEntity("Contact");
        log.info(results.toString());
        results.clear();

        restoreEntity("ItemCategory");
        log.info(results.toString());
        results.clear();
        restoreEntity("ItemType");
        log.info(results.toString());
        results.clear();
        restoreEntity("ItemCode");
        log.info(results.toString());
        results.clear();
        restoreEntity("ItemSellPrice");
        log.info(results.toString());
        results.clear();

        restoreEntity("ItemCodeSupplier");
        log.info(results.toString());
        results.clear();
        restoreEntity("ItemCodeSupplierEquivalent");
        log.info(results.toString());
        results.clear();

        restoreEntity("InventoryIn");
        log.info(results.toString());
        results.clear();
        restoreEntity("Inventory");
        log.info(results.toString());
        results.clear();

        restoreEntity("Sale");
        log.info(results.toString());
        results.clear();
        restoreEntity("SaleArticle");
        log.info(results.toString());
        results.clear();
        restoreEntity("SaleContainer");
        log.info(results.toString());
        results.clear();
        restoreEntity("SaleLot");
        log.info(results.toString());
        results.clear();

        return "Successfully restore backup named: " + backupFile.getName();
    }
    private void restoreEntity(String entityName) throws MismatchedUnitException {

        JsonObject jsEntity = root.get(entityName).getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> entityMemberSet = jsEntity.entrySet();

        for (Map.Entry<String, JsonElement> entry : entityMemberSet) {

            JsonObject fieldMap = entry.getValue().getAsJsonObject();

            switch (entityName) {
                case "Company" -> {
                    persistingCompany(fieldMap);
                }
                case "Address" -> {
                    persistingAddress(fieldMap);
                }
                case "Contact" -> {
                    persistingContact(fieldMap);
                }
                case "ItemCategory" -> {
                    persistingItemCategory(fieldMap);
                }
                case "ItemType" -> {
                    persistingItemType(fieldMap);
                }
                case "ItemCode" -> {
                    persistingItemCode(fieldMap);
                }
                case "ItemSellPrice" -> {
                    persistingItemSellPrice(fieldMap);
                }
                case "ItemCodeSupplier" -> {
                    persistingItemCodeSupplier(fieldMap);
                }
                case "ItemCodeSupplierEquivalent" -> {
                    persistingItemCodeSupplierEquivalent(fieldMap);
                }
                case "InventoryIn" -> {
                    persistingInventoryIn(fieldMap);
                }
                case "Inventory" -> {
                    persistingInventory(fieldMap);
                }
                case "Sale" -> {
                    persistingSale(fieldMap);
                }
                case "SaleArticle" -> {
                    persistingSaleArticle(fieldMap);
                }
                case "SaleContainer" -> {
                    persistingSaleContainer(fieldMap);
                }
                case "SaleLot" -> {
                    persistingSaleLot(fieldMap);
                }
            }
        }
    }


    private void persistingCompany(JsonObject fieldMap) {

        Company company = new Company();
        company.setCompanyType(fieldMap.get("companyType").getAsString());
        company.setCompanyIndustry(fieldMap.get("companyIndustry").getAsString());
        company.setAbbreviation(fieldMap.get("abbreviation").getAsString());
        company.setNameVn(fieldMap.get("nameVn").getAsString());
        company.setNameEn(fieldMap.get("nameEn").getAsString());

        companyClusterService.addNewCompany(results, company);
    }
    private void persistingAddress(JsonObject fieldMap) {

        String idCompanyFromBackup = fieldMap.get("idCompany").getAsString();
        String companyName = root.get("Company").getAsJsonObject().get(idCompanyFromBackup).getAsJsonObject().get("nameEn").getAsString();
        Company company = companyClusterService.findCompanyByCompanyNameEn(results, companyName);

        Address address = new Address();
        address.setAddressVn(fieldMap.get("addressVn").getAsString());
        address.setAddressEn(fieldMap.get("addressEn").getAsString());
        address.setAddressType(fieldMap.get("addressType").getAsString());
        address.setAddressName(fieldMap.get("addressName").getAsString());
        address.setTaxCode(fieldMap.get("taxCode").getAsString());
        address.setRepresentative(fieldMap.get("representative").getAsString());
        address.setOutsideCity(fieldMap.get("outsideCity").getAsBoolean());
        address.setDistance(fieldMap.get("distance") == null ? null : fieldMap.get("distance").getAsFloat());

        log.info(companyClusterService.addNewAddress(address, company.getIdCompany()));
    }
    private void persistingContact(JsonObject fieldMap) {

        String idAddressFromBackup = fieldMap.get("idAddress").getAsString();
        String addressName = root.get("Address").getAsJsonObject().get(idAddressFromBackup).getAsJsonObject().get("addressName").getAsString();
        Address address = companyClusterService.findAddressByAddressName(results, addressName);

        Contact contact = new Contact();
        contact.setContactName(fieldMap.get("contactName").getAsString());
        contact.setEmail(fieldMap.get("email").getAsString());
        contact.setPosition(fieldMap.get("position").getAsString());
        contact.setPhone1(fieldMap.get("phone1").getAsString());
        contact.setBankAccount(fieldMap.get("bankAccount").getAsString());
        contact.setPhone2((fieldMap.get("phone2").getAsString()));

        log.info(companyClusterService.addNewContact(contact, address.getIdAddress()));
    }

    private void persistingItemCategory(JsonObject fieldMap) {
        log.info(itemCategoryService.addNewItemCategory(fieldMap.get("itemCategoryString").getAsString()));
    }
    private void persistingItemType(JsonObject fieldMap) {

        String idItemCategoryFromBackup = fieldMap.get("idItemCategory").getAsString();
        String itemCategoryString = root.get("ItemCategory").getAsJsonObject().get(idItemCategoryFromBackup).getAsJsonObject().get("itemCategoryString").getAsString();
        ItemCategory itemCategory = itemCategoryService.findByItemCategoryString(itemCategoryString);

        log.info(itemTypeService.addNewItemType(itemCategory, fieldMap.get("itemTypeString").getAsString()));
    }
    private void persistingItemCode(JsonObject fieldMap) {

        String idItemTypeFromBackup = fieldMap.get("idItemType").getAsString();
        String itemTypeString = root.get("ItemType").getAsJsonObject().get(idItemTypeFromBackup).getAsJsonObject().get("itemTypeString").getAsString();

        ItemType itemType = itemTypeService.findByItemTypeString(itemTypeString);
        ItemCode itemCode = new ItemCode();
        itemCode.setItemCodeString(fieldMap.get("itemCodeString").getAsString());
        itemCode.setNote(fieldMap.get("note") == null ? null : fieldMap.get("note").getAsString());
        log.info(itemCodeService.addNewItemCode(itemType, itemCode));
    }
    private void persistingItemSellPrice(JsonObject fieldMap) {

        String idCustomerFromBackup = fieldMap.get("idCustomer").getAsString();
        String customerNameEn = root.get("Company").getAsJsonObject().get(idCustomerFromBackup).getAsJsonObject().get("nameEn").getAsString();
        Company customer = companyClusterService.findCompanyByCompanyNameEn(results, customerNameEn);

        List<ItemCode> itemCodeList = new ArrayList<>();
        String[] idItemCodesFromBackup = fieldMap.get("itemCodeList").getAsString().split(",");
        for (String idItemCode : idItemCodesFromBackup) {
            if (idItemCode.equals("")) {
                continue;
            }
            String itemCodeString = root.get("ItemCode").getAsJsonObject().get(idItemCode).getAsJsonObject().get("itemCodeString").getAsString();
            ItemCode itemCode = itemCodeService.findByItemCodeString(results, itemCodeString);
            itemCodeList.add(itemCode);
        }

        ItemSellPrice itemSellPrice = new ItemSellPrice();
        itemSellPrice.setCustomer(customer);
        itemSellPrice.setItemSellPriceContract(fieldMap.get("itemSellPriceContract").getAsString());
        itemSellPrice.setItemSellPriceAmount(fieldMap.get("itemSellPriceAmount").getAsFloat());
        itemSellPrice.setItemSellPriceUnit(fieldMap.get("itemSellPriceUnit").getAsString());

        List<Integer> fromDateInt = Arrays.stream(fieldMap.get("fromDate").getAsString().split("-")).map(Integer::parseInt).toList();
        itemSellPrice.setFromDate(LocalDate.of(fromDateInt.get(0), fromDateInt.get(1), fromDateInt.get(2)));

        List<Integer> toDateInt = Arrays.stream(fieldMap.get("toDate").getAsString().split("-")).map(Integer::parseInt).toList();
        itemSellPrice.setToDate(LocalDate.of(toDateInt.get(0), toDateInt.get(1), toDateInt.get(2)));

        itemSellPrice.setNote(fieldMap.get("note") == null ? null : fieldMap.get("note").getAsString());

        log.info(itemSellPriceService.restoreItemSellPrice(itemSellPrice, itemCodeList));
    }
    private void persistingItemCodeSupplier(JsonObject fieldMap) {


        String idSupplierFromBackup = fieldMap.get("idSupplier").getAsString();
        String supplierNameEn = root.get("Company").getAsJsonObject().get(idSupplierFromBackup).getAsJsonObject().get("nameEn").getAsString();
        Company supplier = companyClusterService.findCompanyByCompanyNameEn(results, supplierNameEn);

        String idItemCodeFromBackUp = fieldMap.get("idItemCode").getAsString();
        String itemCodeString = root.get("ItemCode").getAsJsonObject().get(idItemCodeFromBackUp).getAsJsonObject().get("itemCodeString").getAsString();
        ItemCode itemCode = itemCodeService.findByItemCodeString(results, itemCodeString);

        ItemCodeSupplierDto itemCodeSupplierDto = new ItemCodeSupplierDto();
        itemCodeSupplierDto.setIdItemCode(itemCode.getIdItemCode());
        itemCodeSupplierDto.setIdCompany(supplier.getIdCompany());
        itemCodeSupplierDto.setItemCodeSupplierString(fieldMap.get("itemCodeSupplierString").getAsString());

        log.info(itemCodeSupplierService.addNewItemCodeSupplier(itemCodeSupplierDto));
    }

    private void persistingItemCodeSupplierEquivalent(JsonObject fieldMap) {

        String idItemCodeSupplierFromBackUp = fieldMap.get("idItemCodeSupplier").getAsString();
        String itemCodeSupplierString = root.get("ItemCodeSupplier").getAsJsonObject().get(idItemCodeSupplierFromBackUp).getAsJsonObject().get("itemCodeSupplierString").getAsString();
        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierService.findByItemCodeSupplierString(itemCodeSupplierString);

        ItemCodeSupplierEquivalentDto itemCodeSupplierEquivalentDto = new ItemCodeSupplierEquivalentDto();
        itemCodeSupplierEquivalentDto.setIdSupplier(itemCodeSupplier.getSupplier().getIdCompany());
        itemCodeSupplierEquivalentDto.setIdItemCode(itemCodeSupplier.getItemCode().getIdItemCode());
        itemCodeSupplierEquivalentDto.setIdItemCodeSupplier(itemCodeSupplier.getIdItemCodeSupplier().toString());
        itemCodeSupplierEquivalentDto.setEquivalent(fieldMap.get("equivalent").getAsString());
        itemCodeSupplierEquivalentDto.setEquivalentUnit(fieldMap.get("equivalentUnit").getAsString());
        itemCodeSupplierEquivalentDto.setSourceUnit(fieldMap.get("sourceUnit").getAsString());

        log.info(itemCodeSupplierEquivalentService.addNewItemCodeSupplierEquivalent(itemCodeSupplierEquivalentDto).toString());
    }

    private void persistingInventoryIn(JsonObject fieldMap) throws MismatchedUnitException {

        InventoryInDto inventoryInDto = new InventoryInDto();
        inventoryInDto.setInventoryInSource(fieldMap.get("inventoryInSource").getAsString());
        inventoryInDto.setExchangeRate(fieldMap.get("exchangeRate").getAsString());
        inventoryInDto.setInventoryInValue(fieldMap.get("inventoryInValue").getAsString());
        inventoryInDto.setForeignUnit(fieldMap.get("foreignUnit").getAsString());
        inventoryInDto.setInventoryInValueForeign(fieldMap.get("inventoryInValueForeign").getAsString());
        inventoryInDto.setVoucher(fieldMap.get("voucher").getAsString());

        String idSupplierSourceFromInventoryIn = (fieldMap.get("idSupplierSource").getAsString());
        String supplierSourceNameEn = root.get("Company").getAsJsonObject().get(idSupplierSourceFromInventoryIn).getAsJsonObject().get("nameEn").getAsString();
        Company supplierSource = companyClusterService.findCompanyByCompanyNameEn(results, supplierSourceNameEn);

        inventoryInDto.setSupplierSource(supplierSource.getIdCompany().toString());
        log.info(inventoryInService.addNewInventoryIn(inventoryInDto).toString());
    }

    private void persistingInventory(JsonObject fieldMap) {

        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setUnit(fieldMap.get("unit").getAsString());
        inventoryDto.setSupplierProductionCode(fieldMap.get("productionCode").getAsString());
        inventoryDto.setInitQuantity(fieldMap.get("initQuantity").getAsString());
        inventoryDto.setRemainingQuantity(fieldMap.get("initQuantity").getAsString());
        inventoryDto.setPlacementInWarehouse(fieldMap.get("placementInWarehouse").getAsString());

        String idItemCodeFromBackUp = fieldMap.get("idItemCode").getAsString();
        String idStoredAtAddressFromBackUp = fieldMap.get("idStoredAtAddress").getAsString();
        String idInventoryInFromBackUp = fieldMap.get("idInventoryIn").getAsString();

        String itemCodeString = root.get("ItemCode").getAsJsonObject().get(idItemCodeFromBackUp).getAsJsonObject().get("itemCodeString").getAsString();
        String addressName = root.get("Address").getAsJsonObject().get(idStoredAtAddressFromBackUp).getAsJsonObject().get("addressName").getAsString();
        String inventoryInVoucher = root.get("InventoryIn").getAsJsonObject().get(idInventoryInFromBackUp).getAsJsonObject().get("voucher").getAsString();

        ItemCode itemCode = itemCodeService.findByItemCodeString(results, itemCodeString);
        Address storedAtAddress = companyClusterService.findAddressByAddressName(results, addressName);
        InventoryIn inventoryIn = inventoryInService.findByVoucher(inventoryInVoucher);

        inventoryDto.setIdInventoryIn(inventoryIn.getIdInventoryIn());
        inventoryDto.setIdAddressStoredAt(storedAtAddress.getIdAddress());
        inventoryDto.setIdItemCode(itemCode.getIdItemCode());

        inventoryService.addNewInventory(results, inventoryDto);
    }

    private void persistingSale(JsonObject fieldMap) {

        SaleDto saleDto = new SaleDto();

        saleDto.setDoneDelivery(fieldMap.get("doneDelivery").getAsBoolean());
        saleDto.setNote(fieldMap.get("note").getAsString());
        saleDto.setOrderBatch(fieldMap.get("orderBatch").getAsString());
        saleDto.setOrderName(fieldMap.get("orderName").getAsString());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        saleDto.setOrderDate(LocalDate.parse(fieldMap.get("orderDate").getAsString(), dtf));

        String idCompanySource = fieldMap.get("idCompanySource").getAsString();
        String idCustomer = fieldMap.get("idCustomer").getAsString();
        String idOrderPlacer = fieldMap.get("idOrderPlacer").getAsString();
        String idAddressOrderPlacer = root.get("Contact").getAsJsonObject().get(idOrderPlacer).getAsJsonObject().get("idAddress").getAsString();

        String companySourceNameEn = root.get("Company").getAsJsonObject().get(idCompanySource).getAsJsonObject().get("nameEn").getAsString();
        String customerNameEn = root.get("Company").getAsJsonObject().get(idCustomer).getAsJsonObject().get("nameEn").getAsString();
        String orderPlacerContactName = root.get("Contact").getAsJsonObject().get(idOrderPlacer).getAsJsonObject().get("contactName").getAsString();
        String orderPlacerAddressName = root.get("Address").getAsJsonObject().get(idAddressOrderPlacer).getAsJsonObject().get("addressName").getAsString();

        Company companySource = companyClusterService.findCompanyByCompanyNameEn(results, companySourceNameEn);
        Company customer = companyClusterService.findCompanyByCompanyNameEn(results, customerNameEn);
        Contact orderPlacer = companyClusterService.findContactByContactNameAndAddressName(results, orderPlacerContactName, orderPlacerAddressName);

        saleDto.setIdCompanySource(companySource.getIdCompany().toString());
        saleDto.setIdCustomer(customer.getIdCompany().toString());
        saleDto.setIdContactCustomer(orderPlacer.getIdContact().toString());

        saleService.addNewSaleData(results, saleDto);
    }

    private void persistingSaleArticle(JsonObject fieldMap) {

        SaleArticleDto saleArticleDto = new SaleArticleDto();

        saleArticleDto.setAllowedSurplus(fieldMap.get("allowedSurplus").getAsString());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String requestDeliveryDate = fieldMap.get("requestDeliveryDate").getAsString();
        saleArticleDto.setRequestDeliveryDate(LocalDate.parse(requestDeliveryDate, dtf));

        String idItemCodeFromBackUp = fieldMap.get("idItemCode").getAsString();
        String idSaleFromBackUp = fieldMap.get("idSale").getAsString();

        String itemCodeString = root.get("ItemCode").getAsJsonObject().get(idItemCodeFromBackUp).getAsJsonObject().get("itemCodeString").getAsString();
        String orderName = root.get("Sale").getAsJsonObject().get(idSaleFromBackUp).getAsJsonObject().get("orderName").getAsString();

        ItemCode itemCode = itemCodeService.findByItemCodeString(results, itemCodeString);
        Sale sale = saleService.findByOrderName(results, orderName);

        saleArticleDto.setIdSale(sale.getIdSale());
        saleArticleDto.setIdItemCode(itemCode.getIdItemCode().toString());

        saleArticleService.addNewSaleArticle(results, saleArticleDto);
    }

    private void persistingSaleContainer(JsonObject fieldMap) {

        SaleContainerDto saleContainerDto = new SaleContainerDto();

        saleContainerDto.setContainer(fieldMap.get("container").getAsString());
        saleContainerDto.setOrderUnit(fieldMap.get("orderUnit").getAsString());
        saleContainerDto.setForClaim(fieldMap.get("forClaim").getAsBoolean());

        String idSaleArticleFromBackup = fieldMap.get("idSaleArticle").getAsString();
        String articleIdSale = root.get("SaleArticle").getAsJsonObject().get(idSaleArticleFromBackup).getAsJsonObject().get("idSale").getAsString();
        String articleIdItemCode = root.get("SaleArticle").getAsJsonObject().get(idSaleArticleFromBackup).getAsJsonObject().get("idItemCode").getAsString();

        String orderName = root.get("Sale").getAsJsonObject().get(articleIdSale).getAsJsonObject().get("orderName").getAsString();
        String itemCodeString = root.get("ItemCode").getAsJsonObject().get(articleIdItemCode).getAsJsonObject().get("itemCodeString").getAsString();

        SaleArticle saleArticle = saleArticleService.findByOrderNameAndItemCodeString(results, orderName, itemCodeString);

        saleContainerService.addNewSaleContainer(results, saleContainerDto);
    }

    private void persistingSaleLot(JsonObject fieldMap) throws MismatchedUnitException {

        SaleLotDto saleLotDto = new SaleLotDto();

        saleLotDto.setLotName(fieldMap.get("lotName").getAsString());
        saleLotDto.setDeliveryTurn(fieldMap.get("deliveryTurn").getAsString());
        saleLotDto.setDeliveredQuantity(fieldMap.get("deliveredQuantity").getAsString());
        saleLotDto.setNote(fieldMap.get("note").getAsString());
        saleLotDto.setOrderColor(fieldMap.get("orderColor").getAsString());
        saleLotDto.setOrderQuantity(fieldMap.get("orderQuantity").getAsString());
        saleLotDto.setOrderStyle(fieldMap.get("orderStyle").getAsString());
        saleLotDto.setSupplierSettled(fieldMap.get("supplierSettled").getAsBoolean());

        String deliveryDate = fieldMap.get("deliveryDate").getAsString();
        saleLotDto.setDeliveryDate(LocalDate.parse(deliveryDate, dtf));

        String idSaleContainerFromBackUp = fieldMap.get("idSaleContainer").getAsString();
        String container = root.get("SaleContainer").getAsJsonObject().get(idSaleContainerFromBackUp).getAsJsonObject().get("container").getAsString();
        String containerIdArticle = root.get("SaleContainer").getAsJsonObject().get(idSaleContainerFromBackUp).getAsJsonObject().get("idSaleArticle").getAsString();
        String itemCodeString = root.get("SaleArticle").getAsJsonObject().get(containerIdArticle).getAsJsonObject().get("itemCodeString").getAsString();
        String articleIdSale = root.get("SaleArticle").getAsJsonObject().get(containerIdArticle).getAsJsonObject().get("idSale").getAsString();
        String orderName = root.get("Sale").getAsJsonObject().get(articleIdSale).getAsJsonObject().get("orderName").getAsString();

        String idSupplierFromBackUp = fieldMap.get("idSupplier").getAsString();
        String supplierNameEn = root.get("Company").getAsJsonObject().get(idSupplierFromBackUp).getAsJsonObject().get("nameEn").getAsString();

        String idFromAddressFromBackUp = fieldMap.get("idFromAddress").getAsString();
        String fromAddressName = fieldMap.get("Address").getAsJsonObject().get(idFromAddressFromBackUp).getAsJsonObject().get("addressName").getAsString();
        String idToAddressFromBackUp = fieldMap.get("idToAddress").getAsString();
        String toAddressName = fieldMap.get("Address").getAsJsonObject().get(idToAddressFromBackUp).getAsJsonObject().get("addressName").getAsString();

        String idReceiverFromBackUp = fieldMap.get("idReceiver").getAsString();
        String contactName = root.get("Contact").getAsJsonObject().get(idReceiverFromBackUp).getAsJsonObject().get("contactName").getAsString();

        SaleContainer saleContainer = saleContainerService.findByContainerAndItemCodeStringAndOrderName(results, container, itemCodeString, orderName);
        Company supplier = companyClusterService.findCompanyByCompanyNameEn(results, supplierNameEn);
        Address fromAddress = companyClusterService.findAddressByAddressName(results, fromAddressName);
        Address toAddress = companyClusterService.findAddressByAddressName(results, toAddressName);
        Contact receiver = companyClusterService.findContactByContactNameAndAddressName(results, contactName, toAddressName);

        if (saleContainer == null || supplier == null || fromAddress == null || toAddress == null || receiver == null) {
            results.add("While restoring SaleContainer from Backup");
            return;
        }

        saleLotDto.setIdSaleContainer(saleContainer.getIdSaleContainer());
        saleLotDto.setIdCompanySupplier(supplier.getIdCompany().toString());
        saleLotDto.setIdFromAddress(fromAddress.getIdAddress().toString());
        saleLotDto.setIdToAddress(toAddress.getIdAddress().toString());
        saleLotDto.setIdContactReceiver(receiver.getIdContact().toString());

        SaleLot saleLot = saleLotService.addNewSaleLot(results, saleLotDto);

        List<String> inventoriesOut = Arrays.stream(fieldMap.get("inventoryOutList").getAsString().replace(" ", "").split(",")).toList();
        List<InventoryOut> inventoryOutList = new ArrayList<>();

        for (String idInventoryOutFromBackup : inventoriesOut) {

            if (idInventoryOutFromBackup.equals("")) {
                continue;
            }
            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setInventoryOutPurpose(root.get("InventoryOut").getAsJsonObject().get(idInventoryOutFromBackup).getAsJsonObject().get("inventoryOutPurpose").getAsString());
            inventoryOut.setInventoryOutQuantity(root.get("InventoryOut").getAsJsonObject().get(idInventoryOutFromBackup).getAsJsonObject().get("inventoryOutQuantity").getAsString());
            inventoryOut.setInventoryOutEquivalent(root.get("InventoryOut").getAsJsonObject().get(idInventoryOutFromBackup).getAsJsonObject().get("inventoryOutEquivalent").getAsString());

            String outIdInventory = root.get("InventoryOut").getAsJsonObject().get(idInventoryOutFromBackup).getAsJsonObject().get("idInventory").getAsString();
            String inventoryProductionCode = root.get("Inventory").getAsJsonObject().get(outIdInventory).getAsJsonObject().get("productionCode").getAsString();
            Integer inventoryNumberInBatch = root.get("Inventory").getAsJsonObject().get(outIdInventory).getAsJsonObject().get("numberInBatch").getAsInt();

            Inventory inventory = inventoryService.findByProductionCodeAndNumberInBatch(results, inventoryProductionCode, inventoryNumberInBatch);
            inventoryOut.setInventory(inventory);

            inventoryOut = inventoryOutService.addNewInventoryOut(results, inventoryOut);
            if (inventoryOut == null) {
                continue;
            }

            inventoryOutList.add(inventoryOut);
        }

        saleLot.setInventoryOutList(inventoryOutList);

        saleLotService.save(results, saleLot);
    }

}
