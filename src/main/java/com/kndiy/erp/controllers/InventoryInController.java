package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.InventoryDto;
import com.kndiy.erp.wrapper.InventoryDtoWrapperDto;
import com.kndiy.erp.dto.InventoryInDto;
import com.kndiy.erp.entities.inventoryCluster.Inventory;
import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.inventory.InventoryInService;
import com.kndiy.erp.services.inventory.InventoryService;
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

import java.util.*;

@Slf4j
@Controller
public class InventoryInController {

    @Autowired
    private InventoryInService inventoryInService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CompanyClusterService companyClusterService;

    @GetMapping("/inventories-in/")
    public String showAllInventoriesIn(Model model) {

        Set<Integer> idInventoryInSet = new HashSet<>();

        model.addAttribute("inventoriesIn", inventoryInService.findAllInventoriesIn());
        model.addAttribute("inventoryMap", inventoryService.mapInventoryWrapper(idInventoryInSet));
        model.addAttribute("idInventoryInList", idInventoryInSet);
        model.addAttribute("inventoryInSources", inventoryInService.findAllInventorySources());
        model.addAttribute("suppliers", companyClusterService.findCompaniesByCompanyType("SUPPLIER"));
        model.addAttribute("inventoryInDto", new InventoryInDto());
        model.addAttribute("inventoryDtoWrapperDto", new InventoryDtoWrapperDto());

        return "inventory/inventories-in";
    }

    @GetMapping("/inventories-in/idInventoryIn={idInventoryIn}/inventories-article-number={articleNumber}")
    public String showAddNewInventoriesWindow(Model model, @PathVariable(name = "idInventoryIn") Integer idInventoryIn, @PathVariable(name = "articleNumber") Integer articleNumber) {

        InventoryDtoWrapperDto inventoryDtoWrapperDto = (InventoryDtoWrapperDto) model.asMap().get("submittedWrapperDto");
        Integer newArticleNumber = (Integer) model.asMap().get("newArticleNumber");

        if (inventoryDtoWrapperDto == null) {

            inventoryDtoWrapperDto = new InventoryDtoWrapperDto();
            LinkedList<InventoryDto> inventoryDtoList = new LinkedList<>();
            inventoryDtoWrapperDto.setInventoryDtoList(inventoryDtoList);

            for (int i = 0; i < articleNumber; i ++) {
                inventoryDtoWrapperDto.getInventoryDtoList().add(new InventoryDto());
            }
        }
        else if (newArticleNumber != null) {
            LinkedList<InventoryDto> inventoryDtoList = inventoryDtoWrapperDto.getInventoryDtoList();

            if (inventoryDtoList == null) {
                inventoryDtoList = new LinkedList<>();
            }

            int oldArticleNumber = inventoryDtoList.size();

            int toChange = newArticleNumber - oldArticleNumber;

            while (toChange > 0) {
                inventoryDtoList.add(new InventoryDto());
                toChange--;
            }

            while (toChange < 0) {
                inventoryDtoList.pollLast();
                toChange++;
            }
        }

        model.addAttribute("inventoryDtoWrapperDto", inventoryDtoWrapperDto);
        model.addAttribute("inventoryIn", inventoryInService.findByIdInventoryIn(idInventoryIn));
        model.addAttribute("inventoryInDto", new InventoryInDto());
        model.addAttribute("selfAddresses", companyClusterService.findAddressByCompanyType("SELF"));
        model.addAttribute("units", inventoryService.findAllUnits());

        return "inventory/inventories-in-input";
    }

    @GetMapping("/inventories-in/edit/idInventoryIn={idInventoryIn}/inventories-article-number={articleNumber}")
    public String showEditInventoriesWindow(Model model, @PathVariable(name = "idInventoryIn") Integer idInventoryIn, @PathVariable(name = "articleNumber") Integer articleNumber) {

        InventoryDtoWrapperDto inventoryDtoWrapperDto = inventoryService.getInventoryDtoWrapperDtoByIdInventoryIn(idInventoryIn);

        Integer newArticleNumber = (Integer) model.asMap().get("newArticleNumber");

        if (inventoryDtoWrapperDto == null) {
            inventoryDtoWrapperDto = new InventoryDtoWrapperDto();

            LinkedList<InventoryDto> inventoryDtoList = new LinkedList<>();
            for (int i = 0; i < articleNumber; i ++) {
                inventoryDtoList.add(new InventoryDto());
            }

            inventoryDtoWrapperDto.setInventoryDtoList(inventoryDtoList);
        }
        else {
            //@valid already ensure newArticleNum > oldArticleNum
            int oldArticleNum = inventoryDtoWrapperDto.getInventoryDtoList().size();
            int newArticleNum = articleNumber;
            int toChange = newArticleNum - oldArticleNum;

            while (toChange > 0) {
                inventoryDtoWrapperDto.getInventoryDtoList().add(new InventoryDto());
                toChange--;
            }
        }

        if (newArticleNumber != null) {
            LinkedList<InventoryDto> inventoryDtoList = inventoryDtoWrapperDto.getInventoryDtoList();

            if (inventoryDtoList == null) {
                inventoryDtoList = new LinkedList<>();
            }

            int oldArticleNumber = inventoryDtoList.size();
            int toChange = newArticleNumber - oldArticleNumber;


            while (toChange > 0) {
                inventoryDtoList.add(new InventoryDto());
                toChange--;
            }

            while (toChange < 0) {
                inventoryDtoList.pollLast();
                toChange++;
            }
        }

        model.addAttribute("inventoryDtoWrapperDto", inventoryDtoWrapperDto);
        model.addAttribute("inventoryIn", inventoryInService.findByIdInventoryIn(idInventoryIn));
        model.addAttribute("inventoryInDto", new InventoryInDto());
        model.addAttribute("selfAddresses", companyClusterService.findAddressByCompanyType("SELF"));
        model.addAttribute("units", inventoryService.findAllUnits());

        return "inventory/inventories-in-input-edit";
    }

    @PostMapping("/inventories-in/new")
    public String addNewInventoryIn(@ModelAttribute @Valid InventoryInDto inventoryInDto, Errors errors, RedirectAttributes redirectAttributes) throws MismatchedUnitException {

        Integer idInventoryIn = null;
        Integer articleNumber = null;

        if (!errors.hasErrors()) {
            List<String> results = inventoryInService.addNewInventoryIn(inventoryInDto);
            idInventoryIn = inventoryInService.findByVoucher(inventoryInDto.getVoucher()).getIdInventoryIn();
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = inventoryInService.parseModifyingInventoryInErrors(errors);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Creating New Inventory In");
            return "redirect:/inventories-in/";
        }

        try {
            articleNumber = Integer.parseInt(inventoryInDto.getNumberOfInventoryArticles());
        }
        catch (Exception ex) {
            return "redirect:/inventories-in/";
        }

        return "redirect:/inventories-in/idInventoryIn=" + idInventoryIn + "/inventories-article-number=" + articleNumber;
    }

    @PostMapping("/inventories-in/edit")
    public String editInventoryIn(@ModelAttribute @Valid InventoryInDto inventoryInDto, Errors errors, RedirectAttributes redirectAttributes) throws MismatchedUnitException {

        if (errors.hasErrors()) {
            List<String> errorList = inventoryInService.parseModifyingInventoryInErrors(errors);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Adding New Inventories");
            return "redirect:/inventories-in/";
        }

        List<String> results = inventoryInService.editInventoryIn(inventoryInDto);
        redirectAttributes.addFlashAttribute("results", results);

        Integer idInventoryIn = inventoryInDto.getIdInventoryIn();

        if (!inventoryInDto.getNumberOfInventoryArticles().equals("")) {
            int articleNumber = Integer.parseInt(inventoryInDto.getNumberOfInventoryArticles());
            return "redirect:/inventories-in/edit/idInventoryIn=" + idInventoryIn + "/inventories-article-number=" + articleNumber;
        }

        return "redirect:/inventories-in/";
    }

    @PostMapping("/inventories-in/new-inventories/new")
    public String addNewInventories(@ModelAttribute @Valid InventoryDtoWrapperDto inventoryDtoWrapperDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorList = inventoryService.parseModifyingInventoryErrors(errors);
            redirectAttributes.addFlashAttribute("submittedWrapperDto", inventoryDtoWrapperDto);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Adding New Inventories");

            return "redirect:/inventories-in/idInventoryIn=" + inventoryDtoWrapperDto.getIdInventoryIn()
                    + "/inventories-article-number=" + inventoryDtoWrapperDto.getInventoryDtoList().size();
        }

        Integer idInventoryIn = inventoryDtoWrapperDto.getIdInventoryIn();
        if (!inventoryDtoWrapperDto.getNewArticleNumber().equals("")) {
            Integer articleNumber = Integer.parseInt(inventoryDtoWrapperDto.getNewArticleNumber());

            redirectAttributes.addFlashAttribute("submittedWrapperDto", inventoryDtoWrapperDto);
            redirectAttributes.addFlashAttribute("newArticleNumber", articleNumber);
            return "redirect:/inventories-in/idInventoryIn=" + idInventoryIn + "/inventories-article-number=" + articleNumber;
        }

        List<String> results = inventoryService.addNewInventories(inventoryDtoWrapperDto);
        redirectAttributes.addFlashAttribute("results", results);
        return "redirect:/inventories-in/";
    }

    @PostMapping("/inventories-in/inventories/edit")
    public String editInventories(@ModelAttribute @Valid InventoryDtoWrapperDto inventoryDtoWrapperDto, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            List<String> errorList = inventoryService.parseModifyingInventoryErrors(errors);
            redirectAttributes.addFlashAttribute("submittedWrapperDto", inventoryDtoWrapperDto);
            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Adding New Inventories");

            return "redirect:/inventories-in/edit/idInventoryIn=" + inventoryDtoWrapperDto.getIdInventoryIn()
                    + "/inventories-article-number=" + inventoryDtoWrapperDto.getInventoryDtoList().size();
        }

        Integer idInventoryIn = inventoryDtoWrapperDto.getIdInventoryIn();

        try {
            Integer articleNumber = Integer.parseInt(inventoryDtoWrapperDto.getNewArticleNumber());

            redirectAttributes.addFlashAttribute("submittedWrapperDto", inventoryDtoWrapperDto);
            redirectAttributes.addFlashAttribute("newArticleNumber", articleNumber);
            return "redirect:/inventories-in/edit/idInventoryIn=" + idInventoryIn + "/inventories-article-number=" + articleNumber;
        }
        catch (Exception ex) {
            List<String> results = new ArrayList<>();
            inventoryService.editInventories(results, inventoryDtoWrapperDto);
            redirectAttributes.addFlashAttribute("results", results);
            return "redirect:/inventories-in/";
        }
    }

    @PostMapping("/inventories-in/inventories/delete")
    public String deleteInventory(Integer idInventory, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();

        Inventory inventory = inventoryService.findByIdInventory(results, idInventory);
        Integer idInventoryIn = inventory.getInventoryIn().getIdInventoryIn();

        results.addAll(inventoryService.deleteByIdInventory(idInventory));
        int articleNumber = inventoryService.findByIdInventoryIn(idInventoryIn).size();
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/inventories-in/edit/idInventoryIn=" + idInventoryIn + "/inventories-article-number=" + articleNumber;
    }

    @PostMapping("/inventories-in/delete")
    public String deleteInventoryIn(Integer idInventoryIn, RedirectAttributes redirectAttributes) {

        List<String> results = inventoryInService.deleteByIdInventoryIn(idInventoryIn);
        redirectAttributes.addFlashAttribute("results", results);

        return "redirect:/inventories-in/";
    }
}
