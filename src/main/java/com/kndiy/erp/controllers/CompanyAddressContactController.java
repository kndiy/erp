package com.kndiy.erp.controllers;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;

import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.services.ErrorHandlingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class CompanyAddressContactController {

    @Autowired
    private CompanyClusterService service;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @GetMapping(value = {
            "/companies/",
            "/companies"
    })
    public String showAllCompanies(Model model, @ModelAttribute Company company) {

        model.addAttribute("companies", service.getAllCompanies());
        model.addAttribute("companyTypes", service.getAllEnumCompanyTypes());
        model.addAttribute("companyIndustries", service.getAllEnumCompanyIndustries());

        return "company/companies";
    }

    @PostMapping("/companies/new")
    public String addNewCompany(@Valid @ModelAttribute("company") Company company, Errors errors, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();

        if (!errors.hasErrors()) {
            service.addNewCompany(results, company);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Creating New Company");
        }

        return "redirect:/companies";
    }

    @PostMapping("/companies/edit")
    public String editCompany(@Valid @ModelAttribute("company") Company company, Errors errors, RedirectAttributes redirectAttributes) {

        List<String> results = new ArrayList<>();

        if (!errors.hasErrors()) {
            service.editCompany(results, company);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Editing Company");
        }
        return "redirect:/companies";
    }

    @PostMapping("/companies/delete")
    public String deleteCompany(Integer idCompany) {

        service.deleteCompanyById(idCompany);

        return "redirect:/companies";
    }

    @GetMapping(value = {
            "/companies/{idCompany}/addresses",
            "/companies/{idCompany}/addresses/"
    })
    public String showCompanyAddresses(Model model, @PathVariable("idCompany") Integer idCompany) {

        model.addAttribute("company", service.getCompanyById(idCompany));
        model.addAttribute("address", new Address());
        model.addAttribute("addresses", service.getAddressesByCompanyID(idCompany));

        return "company/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/new")
    public String newAddress(@Valid @ModelAttribute Address address, Errors errors, RedirectAttributes redirectAttributes, @PathVariable("idCompany") Integer idCompany) {

        if (!errors.hasErrors()) {
            List<String> results = new ArrayList<>();
            service.addNewAddress(results, address, idCompany);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Address");
        }

        return "redirect:/companies/{idCompany}/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/edit")
    public String editAddress(@Valid @ModelAttribute Address address, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            List<String> results = new ArrayList<>();
            service.editAddress(results, address);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Editing Address");
        }

        return "redirect:/companies/{idCompany}/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/delete")
    public String deleteAddress(Integer idAddress, RedirectAttributes redirectAttributes) {
        List<String> results = new ArrayList<>();
        service.deleteAddressById(results, idAddress);
        redirectAttributes.addFlashAttribute("results", results);
        return "redirect:/companies/{idCompany}/addresses";
    }

    @GetMapping(value = {
            "/companies/{idCompany}/addresses/{idAddress}/contacts",
            "/companies/{idCompany}/addresses/{idAddress}/contacts/"
    })
    public String showAddressContacts(Model model, @PathVariable("idAddress") Integer idAddress, @PathVariable("idCompany") Integer idCompany) {

        model.addAttribute("contacts", service.getContactsByAddressId(idAddress));
        model.addAttribute("address", service.getAddressById(idAddress));
        model.addAttribute("company", service.getCompanyById(idCompany));
        model.addAttribute("contact", new Contact());

        return "company/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/new")
    public String addNewContact(@Valid @ModelAttribute Contact contact, Errors errors, @PathVariable("idAddress") Integer idAddress, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            List<String> results = new ArrayList<>();
            service.addNewContact(results, contact, idAddress);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Contact");
        }

        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/edit")
    public String editContact(@Valid @ModelAttribute("contact") Contact contact, @PathVariable("idAddress") Integer idAddress, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {

            List<String> results = new ArrayList<>();
            service.editContact(results, contact, idAddress);
            redirectAttributes.addFlashAttribute("results", results);
        }
        else {
            List<String> errorList = errorHandlingService.parseError(errors);

            redirectAttributes.addFlashAttribute("errors", errorList);
            redirectAttributes.addFlashAttribute("errorType", "Editing Contact");
        }

        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/delete")
    public String deleteContact(Integer idContact, RedirectAttributes redirectAttributes) {
        List<String> results = new ArrayList<>();
        service.deleteContactById(results, idContact);
        redirectAttributes.addFlashAttribute("results", results);
        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }
}
