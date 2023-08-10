package com.kndiy.erp.services.inventory;

import com.kndiy.erp.dto.InventoryInDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.entities.inventoryCluster.InventoryIn;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.InventoryInRepository;
import com.kndiy.erp.repositories.InventoryRepository;
import com.kndiy.erp.repositories.ItemCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class InventoryInService {
    @Autowired
    private InventoryInRepository inventoryInRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryOutService inventoryOutService;

    public List<InventoryIn> findAllInventoriesIn() {
        return inventoryInRepository.findAllInventoriesIn();
    }
    public List<Integer> findAllIdInventoriesInThatHasInventories() {
        List<Integer> idList = new ArrayList<>();

        inventoryInRepository.findAllInventoriesIn().forEach(in -> {
            int idInventoryIn = in.getIdInventoryIn();
            List<Inventory> inventoryList = inventoryRepository.findByIdInventoryIn(idInventoryIn);

            if (inventoryList != null && !inventoryList.isEmpty()) {
                idList.add(in.getIdInventoryIn());
            }
        });

        return idList;
    }

    public List<String> findAllInventorySources() {
        InventoryIn.InventorySourceEnum[] sources = InventoryIn.InventorySourceEnum.values();
        return Arrays.stream(sources).map(Enum::toString).toList();
    }


    public List<String> parseModifyingInventoryInErrors(Errors errors) {
        List<String> res = new ArrayList<>();

        errors.getAllErrors().forEach(error -> {
            res.add(error.getDefaultMessage());
        });

        return res;
    }

    public List<String> addNewInventoryIn(InventoryInDto inventoryInDto) throws MismatchedUnitException {
        List<String> results = new ArrayList<>();

        InventoryIn inventoryIn = inventoryInRepository.findByVoucher(inventoryInDto.getVoucher());
        Company supplier = companyRepository.findCompanyByCompanyId(Integer.parseInt(inventoryInDto.getSupplierSource()));

        if (inventoryIn != null) {
            results.add("This voucher already exists under Inventory In Id: " + inventoryIn.getIdInventoryIn());
            return results;
        }

        inventoryIn = new InventoryIn();
        inventoryIn.setInventoryInSource(inventoryInDto.getInventoryInSource());
        inventoryIn.setVoucher(inventoryInDto.getVoucher());
        inventoryIn.setSupplierSource(supplier);

        Quantity value;
        Quantity foreignValue;
        Quantity exchangeRate;

        if (inventoryInDto.getInventoryInValue().equals("")) {
            foreignValue = new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            exchangeRate = new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND", RoundingMode.UP, 4);

            value = foreignValue.times(exchangeRate);
        }
        else {
            value = new Quantity(inventoryInDto.getInventoryInValue(), "VND");
        }

        if (!inventoryInDto.getInventoryInValueForeign().equals("")) {
            foreignValue = new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            exchangeRate = new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND", RoundingMode.UP, 4);
            inventoryIn.setInventoryInValueForeign(foreignValue.toString());
            inventoryIn.setExchangeRate(exchangeRate.toString());
        }

        inventoryIn.setInventoryInValue(value.toString());
        inventoryIn.setForeignUnit(inventoryInDto.getForeignUnit());
        inventoryInRepository.save(inventoryIn);

        results.add("Successfully created new Inventory In, please fill in the Attached Inventories details!");
        return results;
    }

    public InventoryIn findByVoucher(String voucher) {
        return inventoryInRepository.findByVoucher(voucher);
    }

    public InventoryIn findByIdInventoryIn(Integer idInventoryIn) {
        return inventoryInRepository.findById(idInventoryIn).orElse(null);
    }


    public List<String> editInventoryIn(InventoryInDto inventoryInDto) throws MismatchedUnitException {
        List<String> results = new ArrayList<>();

        InventoryIn checkVoucher = inventoryInRepository.findByVoucher(inventoryInDto.getVoucher());

        if (checkVoucher != null) {
            results.add("This Voucher already belongs to InventoryIn with Id: " + checkVoucher.getIdInventoryIn());
            return results;
        }

        InventoryIn inventoryIn = inventoryInRepository.findById(inventoryInDto.getIdInventoryIn()).orElse(null);

        if (inventoryIn == null) {
            results.add("This InventoryIn does not exist anymore!");
            return results;
        }

        Company supplySource = companyRepository.findCompanyByCompanyId(Integer.parseInt(inventoryInDto.getSupplierSource()));

        inventoryIn.setSupplierSource(supplySource);
        inventoryIn.setVoucher(inventoryInDto.getVoucher());
        inventoryIn.setInventoryInSource(inventoryInDto.getInventoryInSource());

        Quantity value;
        Quantity foreignValue;
        Quantity exchangeRate;

        if (inventoryInDto.getInventoryInValue().equals("")) {
            foreignValue = new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            exchangeRate = new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND", RoundingMode.UP, 4);
            value = foreignValue.times(exchangeRate);
        }
        else {
            value = new Quantity(inventoryInDto.getInventoryInValue(), "VND");
        }

        if (!inventoryInDto.getInventoryInValueForeign().equals("")) {
            foreignValue = new Quantity(inventoryInDto.getInventoryInValueForeign(), inventoryInDto.getForeignUnit());
            exchangeRate = new Quantity(inventoryInDto.getExchangeRate(), inventoryInDto.getForeignUnit() + "/VND", RoundingMode.UP, 4);

        }
        else {
            foreignValue = new Quantity(0, inventoryInDto.getForeignUnit());
            exchangeRate = new Quantity(0, inventoryInDto.getForeignUnit() + "/VND", RoundingMode.UP, 4);
        }

        inventoryIn.setInventoryInValue(value.toString());
        inventoryIn.setForeignUnit(inventoryInDto.getForeignUnit());
        inventoryIn.setInventoryInValueForeign(foreignValue.toString());
        inventoryIn.setExchangeRate(exchangeRate.toString());

        inventoryInRepository.save(inventoryIn);

        results.add("Successfully edited InventoryId under Voucher named: " + inventoryIn.getVoucher());

        return results;
    }

    public List<String> deleteByIdInventoryIn(Integer idInventoryIn) {
        List<String> res = new ArrayList<>();

        InventoryIn inventoryIn = inventoryInRepository.findById(idInventoryIn).orElse(null);
        if (inventoryIn == null) {
            res.add("InventoryIn with Id " + idInventoryIn + " does not exist!");
            return res;
        }

        List<InventoryOut> inventoryOutList = inventoryOutService.findByIdInventoryIn(idInventoryIn);

        if (inventoryOutList.isEmpty()) {
            inventoryInRepository.deleteById(idInventoryIn);
            res.add("Successfully deleted Inventory In of Voucher: " + inventoryIn.getVoucher());
        }
        else {
            res.add("Could not delete InventoryIn with Id: " + idInventoryIn + " because its Inventories were used in: ");
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

                res.add("Inventory with Id: " + inventoryOut.getInventory().getIdInventory() + " was used in a "
                        + inventoryOut.getInventoryOutPurpose() + " transaction with id: " + idTransaction + ";");
            }
        }

        return res;
    }
}
