package com.kndiy.erp.services.sales;

import com.kndiy.erp.dto.SaleArticleDto;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.salesCluster.Sale;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.repositories.ItemCodeRepository;
import com.kndiy.erp.repositories.SaleArticleRepository;
import com.kndiy.erp.repositories.SaleContainerRepository;
import com.kndiy.erp.repositories.SaleRepository;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.services.item.ItemCodeService;
import com.kndiy.erp.wrapper.SaleArticleWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleArticleService {

    @Autowired
    private SaleArticleRepository saleArticleRepository;
    @Autowired
    private SaleContainerRepository saleContainerRepository;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private InventoryOutService inventoryOutService;

    public Map<Integer, SaleArticleWrapper> mapSaleArticleWrapperByIdSale(Set<Integer> idSaleSet) {
        Map<Integer, SaleArticleWrapper> map = new HashMap<>();

        List<SaleArticle> saleArticles = saleArticleRepository.findAll(Sort.by("itemCode"));

        saleArticles.forEach(art -> {
            int idSale = art.getSale().getIdSale();
            idSaleSet.add(idSale);

            SaleArticleWrapper saleArticleWrapper = map.get(idSale);

            if (saleArticleWrapper == null) {
                saleArticleWrapper = new SaleArticleWrapper(new ArrayList<SaleArticle>(List.of(art)));
                map.put(idSale, saleArticleWrapper);
                return;
            }

            List<SaleArticle> saleArticleList = saleArticleWrapper.getArticleList();

            if (saleArticleList == null) {
                saleArticleList = new ArrayList<>(List.of(art));
            }
            else {
                saleArticleList.add(art);
            }
        });

        return map;
    }

    public SaleArticle addNewSaleArticle(List<String> results, SaleArticleDto saleArticleDto) {

        ItemCode itemCode = itemCodeService.findByIdItemCode(results, Integer.parseInt(saleArticleDto.getIdItemCode()));
        Sale sale = saleService.findByIdSale(results, saleArticleDto.getIdSale());
        if (itemCode == null || sale == null) {
            results.add("While adding new SaleArticle;");
            return null;
        }

        SaleArticle saleArticle = saleArticleRepository.findByIdSaleAndItemCode(saleArticleDto.getIdSale(), itemCode);

        if (saleArticle != null) {
            results.add("Sale Article corresponds with idSale of: " + saleArticleDto.getIdSale() + " and idItemCode: " + saleArticleDto.getIdItemCode() + " already exists!");
            return saleArticle;
        }

        saleArticle = new SaleArticle();
        saleArticle.setSale(sale);
        saleArticle.setItemCode(itemCode);
        saleArticle.setRequestDeliveryDate(saleArticleDto.getRequestDeliveryDate());
        saleArticle.setAllowedSurplus(Float.parseFloat(saleArticleDto.getAllowedSurplus()));

        results.add("Successfully added new SaleArticle!");

        return saleArticleRepository.save(saleArticle);
    }

    public SaleArticle editSaleArticle(List<String> results, SaleArticleDto saleArticleDto) {

        ItemCode itemCode = itemCodeService.findByIdItemCode(results, Integer.parseInt(saleArticleDto.getIdItemCode()));
        Sale sale = saleService.findByIdSale(results, saleArticleDto.getIdSale());

        SaleArticle saleArticle = saleArticleRepository.findByIdSaleArticle(saleArticleDto.getIdSaleArticle());
        if (saleArticle == null) {
            results.add("Sale Article with idSaleArticle of: " + saleArticleDto.getIdSaleArticle() + " does not exist anymore!");
            return null;
        }

        saleArticle.setItemCode(itemCode);
        saleArticle.setRequestDeliveryDate(saleArticleDto.getRequestDeliveryDate());
        saleArticle.setSale(sale);
        saleArticle.setAllowedSurplus(Float.parseFloat(saleArticleDto.getAllowedSurplus()));
        saleArticleRepository.save(saleArticle);

        results.add("Successfully edited SaleArticle with Id: " + saleArticle.getIdSaleArticle());

        return saleArticle;
    }

    public List<String> deleteSaleArticle(Integer idSaleArticle) throws MismatchedUnitException {
        List<String> res = new ArrayList<>();

        SaleArticle saleArticle = saleArticleRepository.findById(idSaleArticle).orElse(null);

        if (saleArticle == null) {
            res.add("SaleArticle with ID: " + idSaleArticle + " does not exist anymore!");
            return res;
        }

        List<SaleLot> saleLotList = saleLotService.findAllByIdSaleArticle(res, idSaleArticle);
        for (SaleLot saleLot : saleLotList) {
            saleLotService.deleteSaleLot(res, saleLot.getIdSaleLot());
        }

        saleArticleRepository.delete(saleArticle);
        res.add("Successfully deleted SaleArticle with ID: " + saleArticle.getIdSaleArticle());
        return res;
    }

    public SaleArticle findByIdSaleArticle(List<String> res, Integer idSaleArticle) {
        SaleArticle saleArticle = saleArticleRepository.findById(idSaleArticle).orElse(null);

        if (saleArticle == null) {
            res.add("SaleArticle with id:" + idSaleArticle + " does not exist anymore!");
        }

        return saleArticle;

    }

    public SaleArticle findByOrderNameAndItemCodeString(List<String> results, String orderName, String itemCodeString) {
        Sale sale = saleService.findByOrderName(results, orderName);
        ItemCode itemCode = itemCodeService.findByItemCodeString(results, itemCodeString);
        if (sale == null || itemCode == null) {
            results.add("While searching for SaleArticle;");
        }

        return saleArticleRepository.findByOrderNameAndItemCodeString(orderName, itemCodeString);
    }
}
