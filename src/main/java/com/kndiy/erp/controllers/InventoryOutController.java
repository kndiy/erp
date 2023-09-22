package com.kndiy.erp.controllers;

import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.services.inventory.InventoryService;
import com.kndiy.erp.services.sales.SaleLotService;
import com.kndiy.erp.wrapper.InventoryOutDtoWrapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@Slf4j
public class InventoryOutController {
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ErrorHandlingService errorHandlingService;
    @Autowired
    private SaleLotService saleLotService;

    @GetMapping(value = {
            "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/inventories-out/",
            "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/inventories-out"
    })
    public String addNewInventoriesOutWindow(Model model,
                                             @PathVariable(name = "idSaleContainer") Integer idSaleContainer,
                                             @PathVariable(name = "idSaleArticle") Integer idSaleArticle,
                                             @PathVariable(name = "idSaleLot") Integer idSaleLot) throws MismatchedUnitException {

        InventoryOutDtoWrapper inventoryOutDtoWrapper = (InventoryOutDtoWrapper) model.asMap().get("inventoryOutDtoWrapper");

        List<String> results = (List<String>) model.asMap().get("results");
        if (results == null) {
            results = new ArrayList<>();
        }

        if (inventoryOutDtoWrapper == null) {
            inventoryOutDtoWrapper = inventoryService.mapInventoriesToInventoryOutDtoWrapper(results, idSaleLot);
        }

        model.addAttribute("purpose", saleLotService.createPurpose(results, idSaleLot));
        model.addAttribute("summaryQuantity", saleLotService.calSummaryQuantityByIdSaleContainer(results, idSaleContainer));
        model.addAttribute("inventoryOutDtoWrapper", inventoryOutDtoWrapper);
        model.addAttribute("results", results);

        return "inventory-out/inventories-out";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/inventories-out/new")
    String addNewInventoriesOut(@ModelAttribute @Valid InventoryOutDtoWrapper inventoryOutDtoWrapper, Errors errors, RedirectAttributes redirectAttributes) throws MismatchedUnitException {

        if (errors.hasErrors()) {

            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new Inventories Out");
            redirectAttributes.addFlashAttribute("inventoryOutDtOWrapper", inventoryOutDtoWrapper);

            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/inventories-out/";
        }

        List<String> results = new ArrayList<>();
        inventoryOutService.addNewInventoriesOut(results, inventoryOutDtoWrapper);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/inventories-out/delete")
    String deleteInventoriesOut(Integer idInventoryOut, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();

        inventoryOutService.deleteInventoryOut(results, idInventoryOut);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/";
    }
}
