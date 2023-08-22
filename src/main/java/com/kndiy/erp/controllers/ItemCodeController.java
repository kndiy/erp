package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.ItemCategoryTypeCodeDto;
import com.kndiy.erp.dto.ItemCodeSupplierDto;
import com.kndiy.erp.dto.ItemCodeSupplierEquivalentDto;
import com.kndiy.erp.dto.ItemSellPriceDto;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.item.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ItemCodeController {
    @Autowired
    private ItemCategoryService itemCategoryService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;
    @Autowired
    private ItemSellPriceService itemSellPriceService;
    @Autowired
    private ItemCodeSupplierEquivalentService itemCodeSupplierEquivalentService;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @GetMapping(value = {"/item-codes/",
            "/item-codes"})
    public String showItemCodes(Model model) {
        
        model.addAttribute("itemCategories", itemCategoryService.findAllItemCategories());
        model.addAttribute("itemTypes", itemTypeService.findAllItemTypes());
        model.addAttribute("itemCodes", itemCodeService.findAllItemCodes());
        model.addAttribute("itemSellPrices", itemSellPriceService.findAll());

        model.addAttribute("suppliers", companyClusterService.findCompaniesByCompanyType("SUPPLIER"));
        model.addAttribute("customers", companyClusterService.findCompaniesByCompanyType("CUSTOMER"));

        model.addAttribute("itemCategoryTypeCodeDto", new ItemCategoryTypeCodeDto());
        model.addAttribute("itemCodeSupplierDto", new ItemCodeSupplierDto());
        model.addAttribute("itemSellPriceDto", new ItemSellPriceDto());
        model.addAttribute("itemCodeSupplierEquivalentDto", new ItemCodeSupplierEquivalentDto());


        return "item-code/item-codes";
    }
    @GetMapping("/item-codes/idItemCategory={idItemCategory}")
    public String filterItemCodesByItemCategory(Model model, @PathVariable("idItemCategory") Integer idItemCategory) {
        model.addAttribute("itemCategories", itemCategoryService.findAllItemCategories());
        model.addAttribute("itemTypes", itemTypeService.findAllItemTypes());
        model.addAttribute("itemCodes", itemCodeService.findItemCodesByIdItemCategory(idItemCategory));
        model.addAttribute("itemSellPrices", itemSellPriceService.findAll());

        model.addAttribute("suppliers", companyClusterService.findCompaniesByCompanyType("SUPPLIER"));
        model.addAttribute("customers", companyClusterService.findCompaniesByCompanyType("CUSTOMER"));

        model.addAttribute("itemCategoryTypeCodeDto", new ItemCategoryTypeCodeDto());
        model.addAttribute("itemCodeSupplierDto", new ItemCodeSupplierDto());
        model.addAttribute("itemSellPriceDto", new ItemSellPriceDto());
        model.addAttribute("itemCodeSupplierEquivalentDto", new ItemCodeSupplierEquivalentDto());

        return "item-code/item-codes";
    }
    @GetMapping("/item-codes/idItemType={idItemType}")
    public String filterItemCodesByItemType(Model model, @PathVariable("idItemType") Integer idItemType) {
        model.addAttribute("itemCategories", itemCategoryService.findAllItemCategories());
        model.addAttribute("itemTypes", itemTypeService.findAllItemTypes());
        model.addAttribute("itemCodes", itemCodeService.findItemCodesByIdItemType(idItemType));
        model.addAttribute("itemSellPrices", itemSellPriceService.findAll());

        model.addAttribute("suppliers", companyClusterService.findCompaniesByCompanyType("SUPPLIER"));
        model.addAttribute("customers", companyClusterService.findCompaniesByCompanyType("CUSTOMER"));

        model.addAttribute("itemCategoryTypeCodeDto", new ItemCategoryTypeCodeDto());
        model.addAttribute("itemCodeSupplierDto", new ItemCodeSupplierDto());
        model.addAttribute("itemSellPriceDto", new ItemSellPriceDto());
        model.addAttribute("itemCodeSupplierEquivalentDto", new ItemCodeSupplierEquivalentDto());

        return "item-code/item-codes";
    }

    @PostMapping("/item-codes/new")
    public String addNewItemCode(@ModelAttribute @Valid ItemCategoryTypeCodeDto itemCategoryTypeCodeDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = itemCodeService.addNewItemCode(itemCategoryTypeCodeDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemCodeService.getModifiedItemCodeErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating New Item Code");
        }

        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/delete")
    public String deleteItemCode(Integer idItemCode, RedirectAttributes redirectAttributes) {
        String result = itemCodeService.deleteByIdItemCode(idItemCode);
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/delete-item-type")
    public String deleteItemType(Integer idItemType, RedirectAttributes redirectAttributes) {
        String result = itemTypeService.deleteByIdItemType(idItemType);
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/delete-item-category")
    public String deleteItemCategory(Integer idItemCategory, RedirectAttributes redirectAttributes) {
        String result = itemCategoryService.deleteByIdItemCategory(idItemCategory);
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/add-new-item-code-supplier")
    public String addNewItemCodeSupplier(@ModelAttribute @Valid ItemCodeSupplierDto itemCodeSupplierDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = itemCodeSupplierService.addNewItemCodeSupplier(itemCodeSupplierDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemCodeSupplierService.getModifiedItemCodeSupplierErrors(errors);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Item Code Supplier");
        }
        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/add-new-item-sell-price")
    public String addNewItemSellPrice(@ModelAttribute @Valid ItemSellPriceDto itemSellPriceDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = itemSellPriceService.addNewItemSellPrice(itemSellPriceDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemSellPriceService.getModifiedItemCodeSupplierErrors(errors);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Item Code Supplier");
        }

        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/delete-sell-price-contract")
    public String deleteSellPriceContract(Integer idItemSellPrice, RedirectAttributes redirectAttributes) {
        String result;
        if (idItemSellPrice == 0) {
            result = "Please select a Contract to delete!";
        }
        else {
            result = itemSellPriceService.deleteByIdItemSellPrice(idItemSellPrice);
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/item-codes/";
    }

    @PostMapping("/item-codes/edit")
    public String editItemCode(@ModelAttribute @Valid ItemCategoryTypeCodeDto itemCategoryTypeCodeDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = itemCodeService.editItemCode(itemCategoryTypeCodeDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemCodeService.getModifiedItemCodeErrors(errors);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing new Item Code");
        }
        return "redirect:/item-codes/";
    }

    @PostMapping("item-codes/edit-item-code-supplier")
    public String editItemCodeSupplier(@ModelAttribute @Valid ItemCodeSupplierDto itemCodeSupplierDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = itemCodeSupplierService.editItemCodeSupplier(itemCodeSupplierDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemCodeSupplierService.getModifiedItemCodeSupplierErrors(errors);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing new Item Code");
        }
        return "redirect:/item-codes/";
    }
    @PostMapping("item-codes/delete-item-code-supplier")
    public String deleteItemCodeSupplier(Integer idItemCode, Integer idSupplier, RedirectAttributes redirectAttributes) {
        String result = itemCodeSupplierService.deleteItemCodeSupplier(idItemCode, idSupplier);
        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/item-codes/";
    }
    @PostMapping("item-codes/edit-sales-contract")
    public String editSalesContract(@ModelAttribute @Valid ItemSellPriceDto itemSellPriceDto, Errors errors, RedirectAttributes redirectAttributes) {
        if (!errors.hasErrors()) {
            String result = itemSellPriceService.editSalesContract(itemSellPriceDto);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            List<String> fieldErrors = itemCodeSupplierService.getModifiedItemCodeSupplierErrors(errors);
            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing new Item Code");
        }
        return "redirect:/item-codes/";
    }

    @PostMapping("item-codes/remove-linked-contract")
    public String removeLinkedContract(Integer idItemSellPrice, Integer idItemCode, RedirectAttributes redirectAttributes) {
        String result = itemSellPriceService.removeLinkedContract(idItemSellPrice, idItemCode);
        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/item-codes/";
    }

}
