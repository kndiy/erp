package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.SaleContainerDto;
import com.kndiy.erp.entities.salesCluster.SaleContainer;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.sales.SaleContainerService;
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
public class SaleContainerController {

    @Autowired
    private SaleContainerService saleContainerService;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @PostMapping(value = "/sales/{idSale}/articles/{idSaleArticle}/containers/new")
    public String addNewSaleContainer(@ModelAttribute @Valid SaleContainerDto saleContainerDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorList = errorHandlingService.parseError(errors);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Adding New SaleContainer");
            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/";
        }

        List<String> results = new ArrayList<>();

        SaleContainer saleContainer = saleContainerService.addNewSaleContainer(results, saleContainerDto);

        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/" + saleContainer.getIdSaleContainer() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/edit")
    public String editSaleContainer(@ModelAttribute @Valid SaleContainerDto saleContainerDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorList = errorHandlingService.parseError(errors);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Editing New SaleContainer");
            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/";
        }

        List<String> results = new ArrayList<>();
        SaleContainer saleContainer = saleContainerService.editSaleContainer(results, saleContainerDto);

        redirectAttributes.addFlashAttribute("results", results);
        if (saleContainer == null) {
            return "redirect:/sales/{idSale}/articles/{idSaleArticle}/";
        }

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/containers/" + saleContainer.getIdSaleContainer() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/containers/{idSaleContainer}/delete")
    public String deleteSaleContainer(Integer idSaleContainer, RedirectAttributes redirectAttributes) throws MismatchedUnitException {

        List<String> results = saleContainerService.deleteSaleContainer(idSaleContainer);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/{idSaleArticle}/";
    }
}
