package com.kndiy.erp.services.inventory;

import com.kndiy.erp.dto.InventoryDto;
import com.kndiy.erp.repositories.AddressRepository;
import com.kndiy.erp.repositories.InventoryInRepository;
import com.kndiy.erp.repositories.InventoryRepository;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.wrapper.InventoryDtoWrapperDto;
import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.item.ItemCodeSupplierEquivalentService;
import com.kndiy.erp.services.item.ItemCodeSupplierService;
import com.kndiy.erp.services.sales.SaleArticleService;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.wrapper.InventoryMapWrapper;
import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.wrapper.InventoryOutDtoWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.*;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private InventoryInRepository inventoryInRepository;
    @Autowired
    private SaleArticleService saleArticleService;
    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;
    @Autowired
    private ItemCodeSupplierEquivalentService itemCodeSupplierEquivalentService;
    @Autowired
    private InventoryOutService inventoryOutService;

    public TreeSet<String> findAllUnits() {
        TreeSet<String> units = new TreeSet<>();

        List<String> quantityWithUnit = inventoryRepository.findAllUnits();
        for (String s : quantityWithUnit) {
            if (s.split(" ").length == 2) {
                units.add(s.split(" ")[1]);
            }
        }
        return units;
    }

    public List<String> parseModifyingInventoryErrors(Errors errors) {
        List<String> errorList = new ArrayList<>();

        errors.getAllErrors().forEach(error -> {
            errorList.add(error.getDefaultMessage());
        });

        return errorList;
    }

    public List<String> addNewInventories(InventoryDtoWrapperDto inventoryDtoWrapperDto) {

        List<String> res = new ArrayList<>();

        List<InventoryDto> inventoryDtoList = inventoryDtoWrapperDto.getInventoryDtoList();

        for (InventoryDto inventoryDto : inventoryDtoList) {

            String[] unitArr = inventoryDto.getUnit().split(",");

            if (unitArr[0].equals("NewUnit")) {
                inventoryDto.setUnit(unitArr[1].trim().toLowerCase());
            }
            else {
                inventoryDto.setUnit(unitArr[0].trim().toLowerCase());
            }

            inventoryDto.setIdInventoryIn(inventoryDtoWrapperDto.getIdInventoryIn());

            addNewInventory(res, inventoryDto);
        }

        res.add("Successfully added new Inventories!");

        return res;
    }

    public Inventory addNewInventory(List<String> results, InventoryDto inventoryDto) {

        Inventory inventory = inventoryRepository.findByIdInventoryInAndProductionCodeAndNumberInBatch(inventoryDto.getIdInventoryIn(), inventoryDto.getSupplierProductionCode(), inventoryDto.getNumberInBatch());

        if (inventory != null) {
            results.add("Inventory with productionCode: " + inventoryDto.getSupplierProductionCode() + " AND numberInBatch: " + inventoryDto.getNumberInBatch() + " already exists in the same InventoryIn!");
            return null;
        }
        inventory = new Inventory();

        ItemCode itemCode = itemCodeRepository.findById(inventoryDto.getIdItemCode()).orElse(null);
        if (itemCode == null) {
            results.add("Could not find ItemCode with Id: " + inventoryDto.getIdItemCode() + " while adding new Inventory");
            return null;
        }
        Address storedAt = addressRepository.findByIdAddress(inventoryDto.getIdAddressStoredAt());
        if (storedAt == null) {
            results.add("Could not find Address with Id: " + inventoryDto.getIdAddressStoredAt() + " while adding new Inventory");
            return null;
        }
        InventoryIn inventoryIn = inventoryInRepository.findById(inventoryDto.getIdInventoryIn()).orElse(null);
        if (inventoryIn == null) {
            results.add("Could not find InventoryIn with Id: " + inventoryDto.getIdInventoryIn() + " while adding new Inventory");
            return null;
        }

        inventory.setInventoryIn(inventoryIn);
        inventory.setItemCode(itemCode);
        inventory.setStoredAtAddress(storedAt);
        inventory.setPlacementInWarehouse(inventoryDto.getPlacementInWarehouse());
        inventory.setNumberInBatch(inventoryDto.getNumberInBatch());
        inventory.setInitQuantity(new Quantity(inventoryDto.getInitQuantity(), inventoryDto.getUnit()).toString());
        inventory.setRemainingQuantity(inventory.getInitQuantity());
        inventory.setProductionCode(inventoryDto.getSupplierProductionCode());

        inventoryRepository.save(inventory);

        return inventory;
    }

    public Map<Integer, InventoryMapWrapper> mapInventoryWrapper(Set<Integer> idInventoryInSet) {

        Map<Integer, InventoryMapWrapper> map = new HashMap<>();

        List<Inventory> inventories = inventoryRepository.findAll(Sort.by("idInventory"));

        inventories.forEach(inv -> {

            int idInventoryIn = inv.getInventoryIn().getIdInventoryIn();
            idInventoryInSet.add(idInventoryIn);

            InventoryMapWrapper inventoryMapWrapper = map.get(idInventoryIn);

            if (inventoryMapWrapper == null) {
                inventoryMapWrapper = new InventoryMapWrapper(new TreeSet<>(List.of(inv)));
                map.put(idInventoryIn, inventoryMapWrapper);
                return;
            }

            TreeSet<Inventory> inventorySet = inventoryMapWrapper.getInventorySet();

            if (inventorySet == null) {
                inventorySet = new TreeSet<>(List.of(inv));
            }
            else {
                inventorySet.add(inv);
            }

        });

        return map;
    }


    public List<Inventory> findByIdInventoryIn(Integer idInventoryIn) {
        return inventoryRepository.findByIdInventoryIn(idInventoryIn);
    }

    public InventoryDtoWrapperDto getInventoryDtoWrapperDtoByIdInventoryIn(Integer idInventoryIn) {
        InventoryDtoWrapperDto inventoryDtoWrapperDto = new InventoryDtoWrapperDto();

        List<Inventory> inventoryList = inventoryRepository.findByIdInventoryIn(idInventoryIn);

        if (inventoryList == null || inventoryList.isEmpty()) {
            return null;
        }

        LinkedList<InventoryDto> inventoryDtoList = new LinkedList<>();

        inventoryList.forEach(inventory -> {
            inventoryDtoList.add(mapInventoryToInventoryDto(inventory));
        });
        inventoryDtoWrapperDto.setIdInventoryIn(idInventoryIn);
        inventoryDtoWrapperDto.setInventoryDtoList(inventoryDtoList);

        return inventoryDtoWrapperDto;
    }

    public InventoryDto mapInventoryToInventoryDto(Inventory inventory) {

        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setIdItemCode(inventory.getItemCode().getIdItemCode());
        inventoryDto.setIdInventoryIn(inventory.getInventoryIn().getIdInventoryIn());
        inventoryDto.setNumberInBatch(inventory.getNumberInBatch());
        inventoryDto.setIdInventory(inventory.getIdInventory());

        inventoryDto.setInitQuantity(inventory.getInitQuantity().split(" ")[0]);
        if (inventory.getInitQuantity().split(" ").length == 2) {
            inventoryDto.setUnit(inventory.getInitQuantity().split(" ")[1]);
        }

        inventoryDto.setRemainingQuantity(inventory.getRemainingQuantity().split(" ")[0]);

        inventoryDto.setIdAddressStoredAt(inventory.getStoredAtAddress().getIdAddress());
        inventoryDto.setPlacementInWarehouse(inventory.getPlacementInWarehouse());
        inventoryDto.setSupplierProductionCode(inventory.getProductionCode());

        return inventoryDto;
    }

    public List<String> editInventories(List<String> results, InventoryDtoWrapperDto inventoryDtoWrapperDto) {

        List<InventoryDto> inventoryDtoList = inventoryDtoWrapperDto.getInventoryDtoList();

        for (InventoryDto inventoryDto : inventoryDtoList) {

            Inventory inventory;
            inventoryDto.setIdInventoryIn(inventoryDtoWrapperDto.getIdInventoryIn());

            if (inventoryDto.getIdInventory() != null) {
                inventory = inventoryRepository.findById(inventoryDto.getIdInventory()).orElse(null);

                if (inventory != null && !inventory.getInitQuantity().equals(inventory.getRemainingQuantity())) {
                    results.add("Could not edit Inventory with Id: " + inventory.getIdInventory() + " because it has been used!");
                    continue;
                }
            }
            else {
                inventory = new Inventory();
            }

            if (inventory == null) {
                inventory = new Inventory();
            }

            String[] unitArr = inventoryDto.getUnit().split(",");

            if (unitArr[0].equals("NewUnit")) {
                inventoryDto.setUnit(unitArr[1].trim().toLowerCase());
            }
            else {
                inventoryDto.setUnit(unitArr[0].trim().toLowerCase());
            }

            ItemCode itemCode = itemCodeRepository.findById(inventoryDto.getIdItemCode()).orElse(null);
            if (itemCode == null) {
                results.add("Could not find ItemCode with Id: " + inventoryDto.getIdItemCode() + " while adding new Inventory");
                return null;
            }
            Address storedAt = addressRepository.findByIdAddress(inventoryDto.getIdAddressStoredAt());
            if (storedAt == null) {
                results.add("Could not find Address with Id: " + inventoryDto.getIdAddressStoredAt() + " while adding new Inventory");
                return null;
            }
            InventoryIn inventoryIn = inventoryInRepository.findById(inventoryDto.getIdInventoryIn()).orElse(null);
            if (inventoryIn == null) {
                results.add("Could not find InventoryIn with Id: " + inventoryDto.getIdInventoryIn() + " while adding new Inventory");
                return null;
            }

            inventory.setInventoryIn(inventoryIn);
            inventory.setItemCode(itemCode);
            inventory.setStoredAtAddress(storedAt);
            inventory.setPlacementInWarehouse(inventoryDto.getPlacementInWarehouse());
            inventory.setNumberInBatch(inventoryDto.getNumberInBatch());
            inventory.setInitQuantity(new Quantity(inventoryDto.getInitQuantity(), inventoryDto.getUnit()).toString());
            inventory.setRemainingQuantity(inventory.getInitQuantity());
            inventory.setProductionCode(inventoryDto.getSupplierProductionCode());

            inventoryRepository.save(inventory);
        }

        results.add("Successfully edited Inventories!");

        return results;
    }

    public Inventory findByIdInventory(List<String> res, Integer idInventory) {
        Inventory inventory = inventoryRepository.findById(idInventory).orElse(null);

        if (inventory == null) {
            res.add("Could not find Inventory with Id: " + idInventory);
        }

        return inventory;
    }

    public List<String> deleteByIdInventory(Integer idInventory) {
        List<String> results = new ArrayList<>();

        List<InventoryOut> inventoryOutList = inventoryOutService.findByIdInventory(idInventory);

        if (inventoryOutList.isEmpty()) {
            inventoryRepository.deleteById(idInventory);
            results.add("Successfully deleted Inventory with Id: " + idInventory);
        }
        else {
            results.add("Could not delete Inventory with Id: " + idInventory + " because it was used in: ");
            for (InventoryOut inventoryOut : inventoryOutList) {

                int idTransaction;
                if (inventoryOut.getInventoryOutPurpose().equals("SALES")) {
                    idTransaction = inventoryOut.getSaleLotList().get(0).getSaleContainer().getSaleArticle().getSale().getIdSale();
                }
                else if (inventoryOut.getInventoryOutPurpose().equals("MANUFACTURE")) {
                    idTransaction = inventoryOut.getManufactureList().get(0).getIdManufacture();
                }
                else if (inventoryOut.getInventoryOutPurpose().equals("WAREHOUSE_TRANSFER")) {
                    idTransaction = inventoryOut.getWarehouseTransferList().get(0).getIdWarehouseTransfer();
                }
                else {
                    idTransaction = inventoryOut.getGiftOutList().get(0).getIdGiftOut();
                }

                results.add(inventoryOut.getInventoryOutPurpose() + " transaction with id: " + idTransaction + ";");
            }
        }

        return results;
    }

    public List<Inventory> findRemainingByIdSaleLot(SaleLot saleLot) {

        ItemCode itemCode = saleLot.getSaleContainer().getSaleArticle().getItemCode();
        Company supplier = saleLot.getSupplier();

        return inventoryRepository.findRemainingByItemCodeAndSupplier(itemCode, supplier);
    }

    public InventoryOutDtoWrapper mapInventoriesToInventoryOutDtoWrapper(List<String> results, Integer idSaleLot) {

        List<InventoryOutDto> inventoryOutDtoList = new ArrayList<>();

        SaleLot saleLot = saleLotService.findByIdSaleLot(results, idSaleLot);
        if (saleLot == null) {
            results.add("Could not find SaleLot with Id: " + idSaleLot);
            return null;
        }

        List<Inventory> inventoryList = findRemainingByIdSaleLot(saleLot);

        for (int i = 0; i < inventoryList.size(); i ++) {
            Inventory inventory = inventoryList.get(i);

            InventoryOutDto inventoryOutDto = mapInventoryToInventoryOutDto(inventory);

            ItemCode itemCode = inventory.getItemCode();
            Company supplier = saleLot.getSupplier();
            ItemCodeSupplier itemCodeSupplier = itemCodeSupplierService.findByItemCodeAndSupplier(results, itemCode, supplier);

            String inventoryUnit = new Quantity(inventory.getInitQuantity()).getUnit();
            String saleUnit = saleLot.getSaleContainer().getOrderUnit();

            String equivalentValue = itemCodeSupplierEquivalentService.findEquivalentValueByItemCodeSupplierAndUnits(results, itemCodeSupplier, inventoryUnit, saleUnit);
            if (equivalentValue.equals("1")) {
                return null;
            }

            inventoryOutDto.setEquivalentValue(equivalentValue);
            inventoryOutDto.setEquivalentUnit(saleUnit);

            Quantity equivalent = new Quantity(inventory.getRemainingQuantity()).times(Float.parseFloat(equivalentValue));
            equivalent.setUnit(saleUnit);
            inventoryOutDto.setEquivalent(equivalent.toString());

            inventoryOutDtoList.add(inventoryOutDto);
        }
        return new InventoryOutDtoWrapper(inventoryOutDtoList, idSaleLot);
    }

    private InventoryOutDto mapInventoryToInventoryOutDto(Inventory inventory) {

        InventoryOutDto inventoryOutDto = new InventoryOutDto();

        inventoryOutDto.setIdInventory(inventory.getIdInventory());
        inventoryOutDto.setPlacementInWarehouse(inventory.getPlacementInWarehouse());
        inventoryOutDto.setProductionCode(inventory.getProductionCode());
        inventoryOutDto.setStoredAtAddress(inventory.getStoredAtAddress().getAddressName());
        inventoryOutDto.setRemainingQuantity(inventory.getRemainingQuantity());
        inventoryOutDto.setInventoryUnit(new Quantity(inventory.getRemainingQuantity()).getUnit());

        return inventoryOutDto;
    }

    public Inventory findByProductionCodeAndNumberInBatch(List<String> results, String productionCode, Integer numberInBatch) {
        Inventory inventory = inventoryRepository.findByProductionCodeAndNumberInBatch(productionCode, numberInBatch);

        if (inventory == null) {
            results.add("Could not find Inventory with ProductionCode: " + productionCode + " AND NumberInBatch: " + numberInBatch);
        }

        return inventory;
    }
}
