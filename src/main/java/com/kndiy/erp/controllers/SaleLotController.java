package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.SaleLotDto;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.sales.SaleLotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SaleLotController {

    @Autowired
    private SaleLotService saleLotService;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @PostMapping(value = "/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/new")
    public String addNewSaleLot(@ModelAttribute @Valid SaleLotDto saleLotDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new SaleLot");
            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/";
        }

        List<String> results = new ArrayList<>();
        SaleLot saleLot = saleLotService.addNewSaleLot(results, saleLotDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/" + saleLot.getIdSaleLot() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/edit")
    public String editSaleLot(@ModelAttribute @Valid SaleLotDto saleLotDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Adding new SaleLot");
            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/";
        }

        List<String> results = new ArrayList<>();
        SaleLot saleLot = saleLotService.editSaleLot(results, saleLotDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/" + saleLot.getIdSaleLot() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/lots/{idSaleLot}/delete")
    public String deleteSaleLot(Integer idSaleLot, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();
        saleLotService.deleteSaleLot(results, idSaleLot);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/";
    }
}
