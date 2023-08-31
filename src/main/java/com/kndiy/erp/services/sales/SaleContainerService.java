package com.kndiy.erp.services.sales;

import com.kndiy.erp.dto.SaleContainerDto;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.repositories.SaleContainerRepository;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.wrapper.SaleContainerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleContainerService {
    @Autowired
    private SaleContainerRepository saleContainerRepository;
    @Autowired
    private SaleArticleService saleArticleService;
    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private InventoryOutService inventoryOutService;

    public Map<Integer, SaleContainerWrapper> mapSaleContainerWrapperByIdSaleArticle(Set<Integer> idSaleArticleSet) {
        Map<Integer, SaleContainerWrapper> map = new HashMap<>();

        List<SaleContainer> saleContainers = saleContainerRepository.findAll(Sort.by("container"));
        saleContainers.forEach(cont -> {

            int idSaleArticle = cont.getSaleArticle().getIdSaleArticle();
            idSaleArticleSet.add(idSaleArticle);

            SaleContainerWrapper saleContainerWrapper = map.get(idSaleArticle);

            if (saleContainerWrapper == null) {
                saleContainerWrapper = new SaleContainerWrapper(new ArrayList<>(List.of(cont)));
                map.put(idSaleArticle, saleContainerWrapper);
                return;
            }

            List<SaleContainer> saleContainerList = saleContainerWrapper.getSaleContainerList();

            if (saleContainerList == null) {
                saleContainerList = new ArrayList<>(List.of(cont));
            }
            else {
                saleContainerList.add(cont);
            }
        });

        return map;
    }

    public SaleContainer addNewSaleContainer(List<String> results, SaleContainerDto saleContainerDto) {

        String[] unitArr = saleContainerDto.getOrderUnit().split(",");
        if (unitArr[0].equals("NewUnit")) {
            saleContainerDto.setOrderUnit(unitArr[1].trim().toLowerCase());
        }
        else {
            saleContainerDto.setOrderUnit(unitArr[0].trim().toLowerCase());
        }

        SaleArticle saleArticle = saleArticleService.findByIdSaleArticle(results, saleContainerDto.getIdSaleArticle());
        if (saleArticle == null) {
            results.add("While adding new SaleContainer;");
            return null;
        }

        SaleContainer saleContainer = saleContainerRepository.findByContainerAndSaleArticle(saleContainerDto.getContainer(), saleArticle);
        if (saleContainer != null) {
            results.add("SaleContainer corresponds with idSaleArticle of: " + saleContainerDto.getIdSaleArticle() + " and Container named: " + saleContainerDto.getContainer() + " already exists!");
            return saleContainer;
        }

        saleContainer = new SaleContainer();
        saleContainer.setContainer(saleContainerDto.getContainer());
        saleContainer.setSaleArticle(saleArticle);
        saleContainer.setForClaim(saleContainerDto.getForClaim());
        saleContainer.setOrderUnit(saleContainerDto.getOrderUnit());

        results.add("Successfully added new SaleContainer!");

        return saleContainerRepository.save(saleContainer);
    }

    public SaleContainer editSaleContainer(List<String> results, SaleContainerDto saleContainerDto) {

        String[] unitArr = saleContainerDto.getOrderUnit().split(",");
        if (unitArr[0].equals("NewUnit")) {
            saleContainerDto.setOrderUnit(unitArr[1].trim().toLowerCase());
        }
        else {
            saleContainerDto.setOrderUnit(unitArr[0].trim().toLowerCase());
        }

        SaleArticle saleArticle = saleArticleService.findByIdSaleArticle(results, saleContainerDto.getIdSaleArticle());
        if (saleArticle == null) {
            results.add("While editing SaleContainer;");
            return null;
        }
        SaleContainer saleContainer = saleContainerRepository.findByContainerAndSaleArticle(saleContainerDto.getContainer(), saleArticle);

        if (saleContainer == null) {
            results.add("SaleContainer corresponds with idSaleArticle of: " + saleContainerDto.getIdSaleArticle() + " and Container " + saleContainerDto.getContainer() + " does not exist!");
            return null;
        }
        saleContainer = findByIdSaleContainer(results, saleContainerDto.getIdSaleContainer());

        saleContainer.setContainer(saleContainerDto.getContainer());
        saleContainer.setSaleArticle(saleArticle);
        saleContainer.setForClaim(saleContainerDto.getForClaim());

        if (!saleContainerDto.getOrderUnit().equals(saleContainer.getOrderUnit()) && saleContainer.getSaleLotList() != null) {

            for (SaleLot saleLot : saleContainer.getSaleLotList()) {
                if (saleLot.getInventoryOutList() != null && !saleLot.getInventoryOutList().isEmpty()) {
                    results.add("SaleContainer with Id:" + saleContainer.getIdSaleContainer() + " already contained InventoryOut. Therefore its Unit cannot be changed!");
                    return null;
                }
            }

            saleContainer.setOrderUnit(saleContainerDto.getOrderUnit());
        }

        results.add("Successfully edited SaleContainer with id: " + saleContainer.getIdSaleContainer());

        return saleContainerRepository.save(saleContainer);
    }

    public List<String> deleteSaleContainer(Integer idSaleContainer) throws MismatchedUnitException {

        List<String> res = new ArrayList<>();

        SaleContainer saleContainer = saleContainerRepository.findById(idSaleContainer).orElse(null);

        if (saleContainer == null) {
            res.add("Sale data with ID: " + idSaleContainer + " does not exist anymore!");
            return res;
        }

        List<SaleLot> saleLotList = saleLotService.findAllByIdSaleContainer(res, idSaleContainer);
        for (SaleLot saleLot : saleLotList) {
            saleLotService.deleteSaleLot(res, saleLot.getIdSaleLot());
        }

        saleContainerRepository.delete(saleContainer);
        res.add("Successfully deleted Sale Container with ID: " + saleContainer.getIdSaleContainer());
        return res;
    }

    public Set<String> findAllOrderUnits() {
        return saleContainerRepository.findAllOrderUnit();
    }

    public SaleContainer findByIdSaleContainer(List<String> res, Integer idSaleContainer) {

        SaleContainer saleContainer = saleContainerRepository.findById(idSaleContainer).orElse(null);

        if (saleContainer == null) {
            res.add("SaleContainer with Id: " + idSaleContainer + " does not exist anymore!");
        }

        return saleContainer;
    }

    public SaleContainer findByIdSaleContainer(Integer idSaleContainer) {

        return saleContainerRepository.findById(idSaleContainer).orElse(null);
    }
    public SaleContainer findByContainerAndItemCodeStringAndOrderName(List<String> results, String container, String itemCodeString, String orderName) {

        SaleContainer saleContainer = saleContainerRepository.findByContainerAndItemCodeStringAndOrderName(container, itemCodeString, orderName);
        if (saleContainer == null) {
            results.add("Could not find SaleContainer with OrderName: " + orderName + " AND itemCodeString: " + itemCodeString + " AND Container named: " + container);
        }

        return saleContainer;
    }

    public static SaleContainerDto mapSaleContainerDto(SaleContainer saleContainer) {

        SaleContainerDto saleContainerDto = new SaleContainerDto();
        saleContainerDto.setIdSaleContainer(saleContainer.getIdSaleContainer());
        saleContainerDto.setContainer(saleContainer.getContainer());
        saleContainerDto.setIdSaleArticle(saleContainer.getSaleArticle().getIdSaleArticle());
        saleContainerDto.setForClaim(saleContainer.getForClaim());
        saleContainerDto.setOrderUnit(saleContainer.getOrderUnit());

        return saleContainerDto;
    }
}
