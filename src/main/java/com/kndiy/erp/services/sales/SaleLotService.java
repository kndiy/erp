package com.kndiy.erp.services.sales;

import com.kndiy.erp.dto.InventoryOutPurposeDto;
import com.kndiy.erp.dto.SaleLotDto;
import com.kndiy.erp.dto.SummaryQuantityDto;
import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.salesCluster.Sale;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.repositories.SaleArticleRepository;
import com.kndiy.erp.repositories.SaleContainerRepository;
import com.kndiy.erp.repositories.SaleLotRepository;
import com.kndiy.erp.repositories.SaleRepository;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.wrapper.SaleLotWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleLotService {

    @Autowired
    private SaleLotRepository saleLotRepository;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private SaleContainerRepository saleContainerRepository;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private SaleArticleRepository saleArticleRepository;
    public Map<Integer, SaleLotWrapper> mapSaleLotWrapperByIdSaleContainer(Set<Integer> idSaleContainerSet) {
        Map<Integer, SaleLotWrapper> map = new HashMap<>();

        List<SaleLot> saleLots = saleLotRepository.findAll(Sort.by("lotName"));

        saleLots.forEach(lot -> {

            int idSaleContainer = lot.getSaleContainer().getIdSaleContainer();
            idSaleContainerSet.add(idSaleContainer);

            SaleLotWrapper saleLotWrapper = map.get(idSaleContainer);

            if (saleLotWrapper == null) {
                saleLotWrapper = new SaleLotWrapper(new ArrayList<>(List.of(lot)));
                map.put(idSaleContainer, saleLotWrapper);
                return;
            }

            List<SaleLot> saleLotList = saleLotWrapper.getSaleLotList();

            if (saleLotList == null) {
                saleLotList = new ArrayList<>(List.of(lot));
            }
            else {
                saleLotList.add(lot);
            }
        });
        return map;
    }

    public SaleLot addNewSaleLot(List<String> results, SaleLotDto saleLotDto) {

        SaleLot saleLot = saleLotRepository.findByIdSaleContainerAndLotName(saleLotDto.getIdSaleContainer(), saleLotDto.getLotName());
        if (saleLot != null) {
            results.add("SaleLot named: " + saleLot.getLotName() + " under SaleContainer Id: " + saleLotDto.getIdSaleContainer() + " already exist!");
            return saleLot;
        }

        SaleContainer saleContainer = saleContainerRepository.findById(saleLotDto.getIdSaleContainer()).orElse(null);
        if (saleContainer == null) {
            results.add("SaleContainer with Id: " + saleLotDto.getIdSaleContainer() + " does not exist!");
            return null;
        }

        Company supplier = companyClusterService.findCompanyByIdCompany(results, saleLotDto.getIdCompanySupplier());
        Address departure = companyClusterService.findAddressByIdAddress(results, saleLotDto.getIdFromAddress());
        Address destination = companyClusterService.findAddressByIdAddress(results, saleLotDto.getIdToAddress());
        Contact receiver = companyClusterService.findContactByIdContact(results, saleLotDto.getIdContactReceiver());

        saleLot = new SaleLot();
        saleLot.setSupplier(supplier);
        saleLot.setLotName(saleLotDto.getLotName());
        saleLot.setOrderQuantity(new Quantity(saleLotDto.getOrderQuantity(), saleContainer.getOrderUnit()).toString());
        saleLot.setSaleContainer(saleContainer);
        saleLot.setOrderStyle(saleLotDto.getOrderStyle());
        saleLot.setOrderColor(saleLotDto.getOrderColor());
        saleLot.setDeliveryDate(saleLotDto.getDeliveryDate());
        saleLot.setToAddress(destination);
        saleLot.setFromAddress(departure);

        try {
            int turn = Integer.parseInt(saleLotDto.getDeliveryTurn());
            saleLot.setDeliveryTurn(turn);
        }
        catch (NumberFormatException ex) {
            saleLot.setDeliveryTurn(0);
        }

        saleLot.setReceiver(receiver);
        saleLot.setNote(saleLotDto.getNote());

        results.add("Successfully added new SaleLot");

        return saleLotRepository.save(saleLot);
    }

    public SaleLot editSaleLot(List<String> results, SaleLotDto saleLotDto) {

        SaleLot saleLot = saleLotRepository.findByIdSaleContainerAndLotName(saleLotDto.getIdSaleContainer(), saleLotDto.getLotName());
        if (saleLot == null) {
            results.add("SaleLot with Id: " + saleLotDto.getIdSaleLot() + " does not exist anymore!");
            return null;
        }

        SaleContainer saleContainer = saleContainerRepository.findById(saleLotDto.getIdSaleContainer()).orElse(null);
        if (saleContainer == null) {
            results.add("SaleContainer with Id: " + saleLotDto.getIdSaleContainer() + " does not exist!");
            return null;
        }

        Company supplier = companyClusterService.findCompanyByIdCompany(results, saleLotDto.getIdCompanySupplier());
        Address departure = companyClusterService.findAddressByIdAddress(results, saleLotDto.getIdFromAddress());
        Address destination = companyClusterService.findAddressByIdAddress(results, saleLotDto.getIdToAddress());
        Contact receiver = companyClusterService.findContactByIdContact(results, saleLotDto.getIdContactReceiver());

        saleLot.setOrderQuantity(new Quantity(saleLotDto.getOrderQuantity(), saleContainer.getOrderUnit()).toString());
        saleLot.setSaleContainer(saleContainer);
        saleLot.setSupplier(supplier);
        saleLot.setLotName(saleLotDto.getLotName());
        saleLot.setOrderStyle(saleLotDto.getOrderStyle());
        saleLot.setOrderColor(saleLotDto.getOrderColor());
        saleLot.setDeliveryDate(saleLotDto.getDeliveryDate());
        saleLot.setToAddress(destination);
        saleLot.setFromAddress(departure);

        try {
            int turn = Integer.parseInt(saleLotDto.getDeliveryTurn());
            saleLot.setDeliveryTurn(turn);
        }
        catch (NumberFormatException ex) {
            saleLot.setDeliveryTurn(0);
        }

        saleLot.setReceiver(receiver);
        saleLot.setNote(saleLotDto.getNote());
        saleLot.setReceiver(receiver);
        saleLot.setNote(saleLotDto.getNote());

        results.add("Successfully edited SaleLot");

        return saleLotRepository.save(saleLot);
    }

    public boolean deleteSaleLot(List<String> results, Integer idSaleLot) {

        SaleLot saleLot = saleLotRepository.findById(idSaleLot).orElse(null);

        if (saleLot == null) {
            results.add("SaleLot with Id: " + idSaleLot + " does not exist anymore!");
            return false;
        }
        else {

            while (!saleLot.getInventoryOutList().isEmpty()) {
                InventoryOut inventoryOut = saleLot.getInventoryOutList().get(0);
                inventoryOutService.deleteInventoryOut(results, inventoryOut.getIdInventoryOut());
            }

            saleLotRepository.delete(saleLot);
            results.add("Successfully deleted SaleLot with Id: " + idSaleLot);
        }

        return true;
    }

    public SaleLot findByIdSaleLot(List<String> results, Integer idSaleLot) {
        SaleLot saleLot = saleLotRepository.findById(idSaleLot).orElse(null);

        if (saleLot == null) {
            results.add("SaleLot with Id: " + idSaleLot + " does not exist anymore!");
        }

        return saleLot;
    }

    public SummaryQuantityDto calSummaryQuantityByIdSaleContainer(List<String> results, Integer idSaleContainer) throws MismatchedUnitException {

        SaleContainer saleContainer = saleContainerRepository.findById(idSaleContainer).orElse(null);
        if (saleContainer == null) {
            results.add("SaleContainer with Id: " + idSaleContainer + " does not exist!");
            return null;
        }

        String saleUnit = saleContainer.getOrderUnit();

        Quantity sumOrder = new Quantity(0F, saleUnit);
        Quantity sumDelivered = new Quantity(0F, saleUnit);

        List<SaleLot> saleLotList = findAllByIdSaleContainer(results, idSaleContainer);

        for (SaleLot saleLot : saleLotList) {
            sumOrder = sumOrder.plus(new Quantity(saleLot.getOrderQuantity(), saleUnit));

            for (InventoryOut inventoryOut : saleLot.getInventoryOutList()) {
                sumDelivered = sumDelivered.plus(new Quantity(inventoryOut.getInventoryOutEquivalent(), saleUnit));
            }
        }

        Float allowedSurplus = saleContainer.getSaleArticle().getAllowedSurplus();
        Quantity allowedQuantity = sumOrder.times(1F + allowedSurplus);
        Quantity differential = new Quantity(sumDelivered.minus(sumOrder).getQuantityValue(), saleUnit);
        Quantity percentageDiff = differential.percentage(sumOrder);

        return new SummaryQuantityDto(sumOrder.toString(),
                allowedQuantity.toString(),
                sumDelivered.toString(),
                differential.toString(),
                percentageDiff.toString(),
                allowedSurplus.toString(),
                saleUnit);
    }

    public List<SaleLot> findAllByIdSaleContainer(List<String> results, Integer idSaleContainer) {

        SaleContainer saleContainer = saleContainerRepository.findById(idSaleContainer).orElse(null);
        if (saleContainer == null) {
            results.add("SaleContainer with Id: " + idSaleContainer + " does not exist!");
            return new ArrayList<SaleLot>();
        }

        return saleLotRepository.findAllByIdSaleContainer(idSaleContainer);
    }

    public SaleLot save(List<String> res, SaleLot saleLot) {
        res.add("Successfully saved SaleLot with Id: " + saleLot.getIdSaleLot());
        return saleLotRepository.save(saleLot);
    }

    public InventoryOutPurposeDto createPurpose(List<String> results, Integer idSaleLot) {
        InventoryOutPurposeDto purposeDto = new InventoryOutPurposeDto();

        purposeDto.setPurpose("SALES");
        SaleLot saleLot = findByIdSaleLot(results, idSaleLot);
        if (saleLot == null) {
            return purposeDto;
        }

        Map<String, String> info = new LinkedHashMap<>();

        info.put("Customer", saleLot.getSaleContainer().getSaleArticle().getSale().getCustomer().getNameEn());
        info.put("Order Name", saleLot.getSaleContainer().getSaleArticle().getSale().getOrderName());
        info.put("Item Code", saleLot.getSaleContainer().getSaleArticle().getItemCode().getItemCodeString());
        info.put("Container Name", saleLot.getSaleContainer().getContainer());
        info.put("Lot Name", saleLot.getLotName());

        purposeDto.setInfo(info);

        return purposeDto;
    }

    public List<SaleLot> findAllByIdSale(List<String> res, Integer idSale) {
        Sale sale = saleRepository.findById(idSale).orElse(null);

        if (sale == null) {
            res.add("Could not find Sale with Id: " + idSale);
            return new ArrayList<>();
        }

        return saleLotRepository.findAllByIdSale(idSale);
    }

    public List<SaleLot> findAllByIdSaleArticle(List<String> res, Integer idSaleArticle) {
        SaleArticle saleArticle = saleArticleRepository.findByIdSaleArticle(idSaleArticle);

        if (saleArticle == null) {
            res.add("Could not find SaleArticle with Id: " + idSaleArticle);
            return new ArrayList<>();
        }
        return saleLotRepository.findAllByIdSaleArticle(idSaleArticle);
    }
}