package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.ItemCodeSupplierEquivalentDto;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.item.ItemCodeSupplierEquivalentService;
import com.kndiy.erp.services.item.ItemCodeSupplierService;
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
public class ItemCodeSupplierEquivalentController {

    @Autowired
    private ItemCodeSupplierEquivalentService itemCodeSupplierEquivalentService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @GetMapping(value = {
            "item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents",
            "item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents/"
    })
    public String showAllItemCodeSupplierEquivalent(Model model, @PathVariable(name = "idItemCodeSupplier") String idItemCodeSupplier) {

        List<String> results = (List<String>) model.asMap().get("results");

        if (results == null) {
            results = new ArrayList<>();
        }


        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierService.findByIdItemCodeSupplier(results, idItemCodeSupplier);

        model.addAttribute("itemCodeSupplier", itemCodeSupplier);
        model.addAttribute("itemCodeSupplierEquivalent", itemCodeSupplierEquivalentService.findAllByItemCodeSupplier(results, itemCodeSupplier));
        model.addAttribute("itemCodeSupplierEquivalentDto", new ItemCodeSupplierEquivalentDto());
        model.addAttribute("units", itemCodeSupplierEquivalentService.findAllUnits());
        model.addAttribute("results", results);

        return "item-code/equivalents";
    }

    @PostMapping("item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents/new")
    public String addNewEquivalent(@Valid @ModelAttribute ItemCodeSupplierEquivalentDto itemCodeSupplierEquivalentDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new Equivalent");
            return "redirect:/item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents";
        }

        redirectAttributes.addFlashAttribute("results", itemCodeSupplierEquivalentService.addNewItemCodeSupplierEquivalent(itemCodeSupplierEquivalentDto));


        return "redirect:/item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents";
    }

    @PostMapping("item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents/edit")
    public String editEquivalent(@Valid @ModelAttribute ItemCodeSupplierEquivalentDto itemCodeSupplierEquivalentDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new Equivalent");
            return "redirect:/item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents";
        }

        redirectAttributes.addFlashAttribute("results", itemCodeSupplierEquivalentService.editItemCodeSupplierEquivalent(itemCodeSupplierEquivalentDto));

        return "redirect:/item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents";
    }

    @PostMapping("item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents/delete")
    public String deleteEquivalent(Integer idItemCodeSupplierEquivalent, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("results", itemCodeSupplierEquivalentService.deleteEquivalent(idItemCodeSupplierEquivalent));

        return "redirect:/item-codes/item-code-supplier/{idItemCodeSupplier}/equivalents";
    }
}
