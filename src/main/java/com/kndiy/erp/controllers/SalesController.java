package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.SaleArticleDto;
import com.kndiy.erp.dto.SaleContainerDto;
import com.kndiy.erp.dto.SaleDto;
import com.kndiy.erp.dto.SaleLotDto;
import com.kndiy.erp.entities.salesCluster.Sale;
import com.kndiy.erp.others.Quantity;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.services.inventory.InventoryService;
import com.kndiy.erp.services.item.ItemCategoryService;
import com.kndiy.erp.services.item.ItemCodeService;
import com.kndiy.erp.services.item.ItemTypeService;
import com.kndiy.erp.services.sales.SaleArticleService;
import com.kndiy.erp.services.sales.SaleContainerService;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.services.sales.SaleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@Slf4j
public class SalesController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private SaleArticleService saleArticleService;
    @Autowired
    private SaleContainerService saleContainerService;
    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private ErrorHandlingService errorHandlingService;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private ItemCategoryService itemCategoryService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryOutService inventoryOutService;

    @GetMapping(value = {
            "/sales/",
            "/sales/{idSale}/",
            "/sales/{idSale}/articles/",
            "/sales/{idSale}/articles/{idSaleArticle}/",
            "/sales/{idSale}/articles/{idSaleArticle}/containers/",
            "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/",
            "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/",
            "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/"})
    public String showSalesData(Model model) {

        Set<Integer> idSaleSet = new HashSet<>();
        Set<Integer> idSaleArticleSet = new HashSet<>();
        Set<Integer> idSaleContainerSet = new HashSet<>();
        Set<Integer> idSaleLotSet = new HashSet<>();
        Map<Integer, Quantity> saleLotQuantityMap = new HashMap<>();
        Map<Integer, Quantity> saleContainerQuantityMap = new HashMap<>();


        model.addAttribute("sales", saleService.findAllSales());
        model.addAttribute("saleArticleMap", saleArticleService.mapSaleArticleWrapperByIdSale(idSaleSet));
        model.addAttribute("saleContainerMap", saleContainerService.mapSaleContainerWrapperByIdSaleArticle(idSaleArticleSet));
        model.addAttribute("saleLotMap", saleLotService.mapSaleLotWrapperByIdSaleContainer(idSaleContainerSet));
        model.addAttribute("inventoryOutMap", inventoryOutService.mapInventoryOutByIdSaleLot(idSaleLotSet, saleLotQuantityMap, saleContainerQuantityMap));
        model.addAttribute("saleLotQuantityMap", saleLotQuantityMap);
        model.addAttribute("saleContainerQuantityMap", saleContainerQuantityMap);

        model.addAttribute("idSaleSet", idSaleSet);
        model.addAttribute("idSaleArticleSet", idSaleArticleSet);
        model.addAttribute("idSaleContainerSet", idSaleContainerSet);
        model.addAttribute("idSaleLotSet", idSaleLotSet);
        model.addAttribute("units", saleContainerService.findAllOrderUnits());

        model.addAttribute("customers", companyClusterService.findCompaniesByCompanyType("CUSTOMER"));
        model.addAttribute("selves", companyClusterService.findCompaniesByCompanyType("SELF"));
        model.addAttribute("suppliers", companyClusterService.findCompaniesByCompanyType("SELF", "SUPPLIER"));
        model.addAttribute("departureAddresses", companyClusterService.findAddressByCompanyType("SELF", "SUPPLIER"));
        model.addAttribute("destinationAddresses", companyClusterService.findAddressByCompanyType("CUSTOMER"));
        model.addAttribute("receiverContacts", companyClusterService.findContactsByCompanyType("CUSTOMER"));

        model.addAttribute("itemCategories", itemCategoryService.findAllItemCategories());
        model.addAttribute("itemTypes", itemTypeService.findAllItemTypes());
        model.addAttribute("itemCodes", itemCodeService.findAllItemCodes());

        model.addAttribute("saleDto", new SaleDto());
        model.addAttribute("saleArticleDto", new SaleArticleDto());
        model.addAttribute("saleContainerDto", new SaleContainerDto());
        model.addAttribute("saleLotDto", new SaleLotDto());

        return "sale/sale";
    }


    @PostMapping("/sales/new")
    public String addNewSaleData(@Valid @ModelAttribute SaleDto saleDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new Sale Data");
            return "redirect:/sales/";
        }

        List<String> results = new ArrayList<>();
        Sale sale = saleService.addNewSaleData(results, saleDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/" + sale.getIdSale() + "/";
    }

    @PostMapping("sales/{idSale}/edit")
    public String editSaleData(@Valid @ModelAttribute SaleDto saleDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new Sale Data");
            return "redirect:/sales/";
        }

        List<String> results = new ArrayList<>();
        Sale sale = saleService.editSaleData(results, saleDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/" + sale.getIdSale() + "/";
    }

    @PostMapping("sales/{idSale}/delete")
    public String deleteSaleData(Integer idSale, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();
        saleService.deleteSaleData(results, idSale);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/";
    }

}
