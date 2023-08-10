package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.SaleArticleDto;
import com.kndiy.erp.entities.salesCluster.SaleArticle;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.sales.SaleArticleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class SaleArticleController {

    @Autowired
    private ErrorHandlingService errorHandlingService;
    @Autowired
    private SaleArticleService saleArticleService;

    @PostMapping(value = "/sales/{idSale}/articles/new")
    public String addNewSaleArticle(@Valid @ModelAttribute SaleArticleDto saleArticleDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorlist = errorHandlingService.parseError(errors);
            redirectAttributes.addFlashAttribute("errors", errorlist);
            redirectAttributes.addFlashAttribute("errorType", "Adding new SaleArticle");
            return "redirect:/sales/{idSale}/";
        }

        List<String> results = new ArrayList<>();
        SaleArticle saleArticle = saleArticleService.addNewSaleArticle(results, saleArticleDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/" + saleArticle.getIdSaleArticle() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/edit")
    public String editSaleArticle(@Valid @ModelAttribute SaleArticleDto saleArticleDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorlist = errorHandlingService.parseError(errors);
            redirectAttributes.addFlashAttribute("errors", errorlist);
            redirectAttributes.addFlashAttribute("errorType", "Editing SaleArticle");
            return "redirect:/sales/{idSale}/";
        }

        List<String> results = new ArrayList<>();
        SaleArticle saleArticle = saleArticleService.editSaleArticle(results, saleArticleDto);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/articles/" + saleArticle.getIdSaleArticle() + "/";
    }

    @PostMapping("/sales/{idSale}/articles/{idSaleArticle}/delete")
    public String deleteSaleArticle(Integer idSaleArticle, RedirectAttributes redirectAttributes) throws MismatchedUnitException {

        List<String> results = saleArticleService.deleteSaleArticle(idSaleArticle);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/sales/{idSale}/";
    }
}
