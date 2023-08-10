package com.kndiy.erp.services.inventory;

import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.repositories.InventoryOutRepository;
import com.kndiy.erp.repositories.InventoryRepository;
import com.kndiy.erp.repositories.SaleLotRepository;
import com.kndiy.erp.wrapper.InventoryOutDtoWrapper;
import com.kndiy.erp.wrapper.InventoryOutWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
public class InventoryOutService {

    @Autowired
    private InventoryOutRepository inventoryOutRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private SaleLotRepository saleLotRepository;

    public Map<Integer, InventoryOutWrapper> mapInventoryOutByIdSaleLot(Set<Integer> idSaleLotSet,
                                                                        Map<Integer, Quantity> saleLotQuantityMap,
                                                                        Map<Integer, Quantity> saleContainerQuantityMap) {
        Map<Integer, InventoryOutWrapper> map = new HashMap<>();

        List<InventoryOut> inventoryOuts = inventoryOutRepository.findAll();
        inventoryOuts.forEach(inv -> {

            if (inv.getSaleLotList() == null || inv.getSaleLotList().isEmpty()) {
                return;
            }

            int idSaleLot = inv.getSaleLotList().get(0).getIdSaleLot();
            int idSaleContainer = inv.getSaleLotList().get(0).getSaleContainer().getIdSaleContainer();
            String saleUnit = inv.getSaleLotList().get(0).getSaleContainer().getOrderUnit();

            idSaleLotSet.add(idSaleLot);

            InventoryOutWrapper inventoryOutWrapper = map.get(idSaleLot);

            if (inventoryOutWrapper == null) {
                inventoryOutWrapper = new InventoryOutWrapper(new ArrayList<>(List.of(inv)));
                map.put(idSaleLot, inventoryOutWrapper);
                return;
            }

            List<InventoryOut> inventoryOutList = inventoryOutWrapper.getInventoryOutList();

            if (inventoryOutList == null) {
                inventoryOutList = new ArrayList<>(List.of(inv));
            }
            else {
                inventoryOutList.add(inv);
            }

            Quantity saleLotQuantity = saleLotQuantityMap.get(idSaleLot);
            if (saleLotQuantity == null) {
                saleLotQuantityMap.put(idSaleLot, new Quantity(inv.getInventoryOutEquivalent(), saleUnit));
            }
            else {
                try {
                    saleLotQuantity = saleLotQuantity.plus(new Quantity(inv.getInventoryOutEquivalent(), saleUnit));
                    saleLotQuantityMap.put(idSaleLot, saleLotQuantity);
                } catch (MismatchedUnitException e) {
                    throw new RuntimeException(e);
                }
            }

            Quantity saleContainerQuantity = saleContainerQuantityMap.get(idSaleContainer);
            if (saleContainerQuantity == null) {
                saleContainerQuantityMap.put(idSaleContainer, new Quantity(inv.getInventoryOutEquivalent(), saleUnit));
            }
            else {
                try {
                    saleContainerQuantity = saleContainerQuantity.plus(new Quantity(inv.getInventoryOutEquivalent(), saleUnit));
                    saleContainerQuantityMap.put(idSaleContainer, saleContainerQuantity);
                } catch (MismatchedUnitException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return map;
    }

    public List<String> addNewInventoriesOut(List<String> results, InventoryOutDtoWrapper inventoryOutDtoWrapper) throws MismatchedUnitException {

        SaleLot saleLot;

        if (inventoryOutDtoWrapper.getIdSaleLot() != null) {
            saleLot = saleLotRepository.findById(inventoryOutDtoWrapper.getIdSaleLot()).orElse(null);
            if (saleLot == null) {
                results.add("Failed to updated SaleLot with chosen InventoriesOut as SaleLot with Id: " + inventoryOutDtoWrapper.getIdSaleLot() + " does not exist!");
                return results;
            }

            List<InventoryOut> inventoryOutList = parseInventoryOutDtoWrapper(results, inventoryOutDtoWrapper);

            if (saleLot.getInventoryOutList() == null) {
                saleLot.setInventoryOutList(inventoryOutList);
            }
            else {
                saleLot.getInventoryOutList().addAll(inventoryOutList);
            }

            results.add("Successfully added new Inventories for SaleLot with Id: " + inventoryOutDtoWrapper.getIdSaleLot());

            saleLotRepository.save(saleLot);
        }

        if (inventoryOutDtoWrapper.getIdManufacture() != null) {

        }

        if (inventoryOutDtoWrapper.getIdWarehouseTransfer() != null) {

        }

        return results;
    }

    private List<InventoryOut> parseInventoryOutDtoWrapper(List<String> results, InventoryOutDtoWrapper inventoryOutDtoWrapper) throws MismatchedUnitException {

        List<InventoryOutDto> inventoryOutDtoList = inventoryOutDtoWrapper.getInventoryOutDtoList();
        List<InventoryOut> inventoryOutList = new ArrayList<>();

        for (InventoryOutDto dto : inventoryOutDtoList) {

            if (!dto.getTakeAll() && !dto.getSplit()) {
                continue;
            }

            Inventory inventory = inventoryRepository.findById(dto.getIdInventory()).orElse(null);
            if (inventory == null) {
                results.add("Skipped InventoryOut belonged to Inventory with Id: " + dto.getIdInventory() + " as Inventory with that Id could not be found!");
                continue;
            }

            InventoryOut inventoryOut = new InventoryOut();
            inventoryOut.setInventory(inventory);

            if (inventoryOutDtoWrapper.getIdSaleLot() != null) {
                inventoryOut.setInventoryOutPurpose("SALES");
            }
            if (inventoryOutDtoWrapper.getIdManufacture() != null) {
                inventoryOut.setInventoryOutPurpose("MANUFACTURE");
            }
            if (inventoryOutDtoWrapper.getIdWarehouseTransfer() != null) {
                inventoryOut.setInventoryOutPurpose("WAREHOUSE_TRANSFER");
            }

            Quantity out;
            Quantity equiv;

            if (dto.getTakeAll()) {
                out = new Quantity(dto.getRemainingQuantity(), dto.getInventoryUnit(), RoundingMode.UP, 2);
                equiv = new Quantity(dto.getEquivalentQuantity(), dto.getEquivalentUnit(), RoundingMode.DOWN, 2);
            }
            else {
                equiv = new Quantity(dto.getSplitQuantity(), dto.getEquivalentUnit(), RoundingMode.DOWN, 2);
                out = new Quantity(equiv.divides(Float.parseFloat(dto.getEquivalentValue()), RoundingMode.UP).toString(), dto.getInventoryUnit());
            }

            inventoryOut.setInventoryOutQuantity(out.toString());
            inventory.setRemainingQuantity(new Quantity(inventory.getRemainingQuantity(), inventory.getUnit()).minus(out).toString());
            inventoryOut.setInventoryOutEquivalent(equiv.toString());
            inventoryOut = inventoryOutRepository.save(inventoryOut);

            inventoryOutList.add(inventoryOut);
        }

        return inventoryOutList;
    }

    public boolean deleteInventoryOut(List<String> results, Integer idInventoryOut) {

        InventoryOut inventoryOut = findByIdInventoryOut(results, idInventoryOut);

        if (inventoryOut == null) {
            results.add("Failed to delete InventoryOut with Id: " + idInventoryOut + " because it does not exist!");
            return false;
        }

        List<SaleLot> saleLotList = inventoryOut.getSaleLotList();
        if (saleLotList != null && !saleLotList.isEmpty()) {
            SaleLot saleLot = saleLotList.get(0);
            saleLot.getInventoryOutList().remove(inventoryOut);
            saleLotRepository.save(saleLot);
        }

        Quantity quantity = new Quantity(inventoryOut.getInventoryOutQuantity(), inventoryOut.getInventory().getUnit());

        Inventory inventory = inventoryOut.getInventory();
        try {
            inventory.setRemainingQuantity(new Quantity(inventory.getRemainingQuantity(), inventory.getUnit()).plus(quantity).toString());
        } catch (MismatchedUnitException e) {
            log.info("Unit not matched?");
        }

        inventoryRepository.save(inventory);
        inventoryOutRepository.delete(inventoryOut);

        results.add("Successfully deleted InventoryOut with Id: " + idInventoryOut);

        return true;
    }

    private InventoryOut findByIdInventoryOut(List<String> results, Integer idInventoryOut) {
        InventoryOut inventoryOut = inventoryOutRepository.findById(idInventoryOut).orElse(null);

        if (inventoryOut == null) {
            results.add("Could not find InventoryOut with Id: " + idInventoryOut);
        }

        return inventoryOut;
    }

    public InventoryOut addNewInventoryOut(List<String> results, InventoryOut inventoryOut) throws MismatchedUnitException {

        String inventoryUnit = inventoryOut.getInventory().getUnit();
        Quantity inventoryRemainingQuantity = new Quantity(inventoryOut.getInventory().getRemainingQuantity(), inventoryUnit);
        Quantity inventoryOutQuantity = new Quantity(inventoryOut.getInventoryOutQuantity(), inventoryUnit);

        if (inventoryRemainingQuantity.lessThan(inventoryOutQuantity)) {
            results.add("Could not export inventoryOut from Inventory with Id: " + inventoryOut.getInventory().getIdInventory() + " as remainingQuantity is less than export Quantity");
            return null;
        }

        inventoryRemainingQuantity = inventoryRemainingQuantity.minus(inventoryOutQuantity);

        inventoryOut.getInventory().setRemainingQuantity(inventoryRemainingQuantity.toString());
        inventoryRepository.save(inventoryOut.getInventory());

        inventoryOutRepository.save(inventoryOut);

        return inventoryOut;
    }

    public List<InventoryOut> findByIdInventoryIn(Integer idInventoryIn) {

        return inventoryOutRepository.findAllByIdInventoryIn(idInventoryIn);
    }

    public List<InventoryOut> findByIdInventory(Integer idInventory) {

        return inventoryOutRepository.findByIdInventory(idInventory);
    }
}
