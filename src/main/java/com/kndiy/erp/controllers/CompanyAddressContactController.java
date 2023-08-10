package com.kndiy.erp.controllers;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;

import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.services.CompanyClusterService;
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

    @GetMapping("/companies/")
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
            ArrayList<String> fieldErrors = service.getModifiedCompanyErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating New Company");
        }

        return "redirect:/companies";
    }

    @PostMapping("/companies/edit")
    public String editCompany(@Valid @ModelAttribute("company") Company company, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = service.editCompany(company);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            ArrayList<String> fieldErrors = service.getModifiedCompanyErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing Company");
        }
        return "redirect:/companies";
    }

    @PostMapping("/companies/delete")
    public String deleteCompany(Integer idCompany) {
        service.deleteCompanyById(idCompany);
        return "redirect:/companies";
    }

    @GetMapping("/companies/{idCompany}/addresses")
    public String showCompanyAddresses(Model model, @PathVariable("idCompany") Integer idCompany, @ModelAttribute Address address) {

        model.addAttribute("abbreviation", service.getCompanyAbbreviationByID(idCompany));
        model.addAttribute("companyName", service.findCompanyNameById(idCompany));
        model.addAttribute("addresses", service.getAddressesByCompanyID(idCompany));

        return "/company/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/new")
    public String newAddress(@Valid @ModelAttribute("address") Address address, Errors errors, RedirectAttributes redirectAttributes, @PathVariable("idCompany") Integer idCompany) {

        if (!errors.hasErrors()) {
            String result = service.addNewAddress(address, idCompany);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            ArrayList<String> fieldErrors = service.getModifiedAddressErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Address");
        }

        return "redirect:/companies/{idCompany}/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/edit")
    public String editAddress(@Valid @ModelAttribute("address") Address address, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = service.editAddress(address);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            ArrayList<String> fieldErrors = service.getModifiedAddressErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing Address");
        }

        return "redirect:/companies/{idCompany}/addresses";
    }

    @PostMapping("/companies/{idCompany}/addresses/delete")
    public String deleteAddress(Integer idAddress, RedirectAttributes redirectAttributes) {
        String result = service.deleteAddressById(idAddress);
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/companies/{idCompany}/addresses";
    }

    @GetMapping("/companies/{idCompany}/addresses/{idAddress}/contacts")
    public String showAddressContacts(Model model, @ModelAttribute Contact contact, @PathVariable("idAddress") Integer idAddress, @PathVariable("idCompany") Integer idCompany) {

        model.addAttribute("contacts", service.getContactsByAddressId(idAddress));
        model.addAttribute("addressName", service.findAddressNameByIdAddress(idAddress));
        model.addAttribute("companyName", service.findCompanyNameById(idCompany));

        return "/company/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/new")
    public String addNewContact(@Valid @ModelAttribute("contact") Contact contact, Errors errors, @PathVariable("idAddress") Integer idAddress, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = service.addNewContact(contact, idAddress);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            ArrayList<String> fieldErrors = service.getModifiedContactErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Creating new Contact");
        }

        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/edit")
    public String editContact(@Valid @ModelAttribute("contact") Contact contact, @PathVariable("idAddress") Integer idAddress, Errors errors, RedirectAttributes redirectAttributes) {

        if (!errors.hasErrors()) {
            String result = service.editContact(contact, idAddress);
            redirectAttributes.addFlashAttribute("result", result);
        }
        else {
            ArrayList<String> fieldErrors = service.getModifiedContactErrors(errors);

            redirectAttributes.addFlashAttribute("fieldErrors", fieldErrors);
            redirectAttributes.addFlashAttribute("errorType", "Editing Contact");
        }

        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }

    @PostMapping("/companies/{idCompany}/addresses/{idAddress}/contacts/delete")
    public String deleteContact(Integer idContact, RedirectAttributes redirectAttributes) {
        String result = service.deleteContactById(idContact);
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/companies/{idCompany}/addresses/{idAddress}/contacts";
    }
}
