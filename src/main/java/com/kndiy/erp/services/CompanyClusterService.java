package com.kndiy.erp.services;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.repositories.AddressRepository;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Service
public class CompanyClusterService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ContactRepository contactRepository;

    public TreeSet<Company> getAllCompanies() {
        Comparator<Company> comp = Comparator.comparingInt(Company::getIdCompany);
        TreeSet<Company> companies = new TreeSet<>(comp);

        companies.addAll(companyRepository.findAll());

        return companies;
    }

    public Company findCompanyByCompanyNameEn(List<String> results, String nameEn) {

        Company company = companyRepository.findCompanyByNameEn(nameEn);
        if (company == null) {
            results.add("Could not find Company with nameEn: " + nameEn);
        }

        return company;
    }

    public Company findCompanyByAbbreviation(String abbreviation) {
        return companyRepository.findByAbbreviation(abbreviation);
    }
    public Company findCompanyByCompanyNameVn(String nameVn) {
        return companyRepository.findCompanyByNameVn(nameVn);
    }
    public TreeSet<String> getAllEnumCompanyTypes() {
        TreeSet<String> companyTypes = new TreeSet<>();
        Company.CompanyTypeEnum[] types = Company.CompanyTypeEnum.values();

        for (Company.CompanyTypeEnum type : types) {
            companyTypes.add(type.toString());
        }

        return companyTypes;
    }

    public TreeSet<String> getAllEnumCompanyIndustries() {
        TreeSet<String> companyIndustries = new TreeSet<>();
        Company.CompanyIndustryEnum[] industries = Company.CompanyIndustryEnum.values();

        for (Company.CompanyIndustryEnum industry : industries) {
            companyIndustries.add(industry.toString());
        }

        return companyIndustries;
    }

    public String getCompanyAbbreviationByID(Integer id) {
        Company company = companyRepository.findById(id).orElse(null);

        if (company == null) {
            return "";
        }

        return company.getAbbreviation();
    }

    public TreeSet<Address> getAddressesByCompanyID(Integer id) {
        Comparator<Address> comp = Comparator.comparingInt(Address::getIdAddress);
        TreeSet<Address> addresses = new TreeSet<>(comp);

        addresses.addAll(addressRepository.findByCompanyId(id));

        return addresses;
    }
    public Company addNewCompany(List<String> results, Company company) {
        Company check = companyRepository.findCompanyByNameEn(company.getNameEn());

        if (check == null) {
            check = companyRepository.save(company);
            results.add("Successfully added new Company named: " + check.getNameEn());
        }
        else {
            results.add("Company with nameEn: " + company.getNameEn() + " already exists!");
        }

        return check;
    }

    public String editCompany(Company company) {
        Company curEditing = companyRepository.findById(company.getIdCompany()).orElse(null);

        if (curEditing != null) {
            curEditing.setCompanyType(company.getCompanyType());
            curEditing.setAbbreviation(company.getAbbreviation());
            curEditing.setCompanyIndustry(company.getCompanyIndustry());
            curEditing.setNameEn(company.getNameEn());
            curEditing.setNameVn(company.getNameVn());
            companyRepository.save(curEditing);
        }
        else {
            return "No such existing Company!";
        }

        return "Successfully edited new Company named: " + curEditing.getNameEn();
    }

    public void deleteCompanyById(Integer idCompany) {
        companyRepository.deleteById(idCompany);
    }

    public ArrayList<String> getModifiedCompanyErrors(Errors errors) {
        ArrayList<String> res = new ArrayList<>();

        errors.getFieldErrors().forEach(error -> res.add(error.getDefaultMessage()));

        return res;
    }

    public TreeSet<Contact> getContactsByAddressId(Integer id) {
        Comparator<Contact> comp = Comparator.comparingInt(Contact::getIdContact);
        TreeSet<Contact> res = new TreeSet<>(comp);
        res.addAll(contactRepository.findAllByAddressID(id));
        return res;
    }

    public String findAddressNameByIdAddress(Integer id) {
        return addressRepository.findByIdAddress(id).getAddressName();
    }

    public Address findAddressByAddressName(List<String> results, String addressName) {

        Address address = addressRepository.findByAddressName(addressName);
        if (address == null) {
            results.add("Could not find Address with addressName: " + addressName);
        }
        return address;
    }
    public String findCompanyNameById(Integer id) {
        return companyRepository.findCompanyByCompanyId(id).getNameEn();
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
    public String editAddress(Address address) {
        Address editingAddress = addressRepository.findById(address.getIdAddress()).orElse(null);

        if (editingAddress != null) {
            editingAddress.setAddressName(address.getAddressName());
            editingAddress.setTaxCode(address.getTaxCode());
            editingAddress.setAddressType(address.getAddressType());
            editingAddress.setAddressVn(address.getAddressVn());
            editingAddress.setAddressEn(address.getAddressEn());
            editingAddress.setDistance(address.getDistance());
            editingAddress.setRepresentative(address.getRepresentative());
            editingAddress.setOutsideCity(address.getOutsideCity());

            saveAddress(editingAddress);
        }
        else {
            return "Could not find such existing Address";
        }
        return "Successfully edited address named: " + editingAddress.getAddressName();
    }

    public String addNewAddress(Address address, Integer idCompany) {
        Address checkExistingAddress = addressRepository.findByAddressName(address.getAddressName());
        if (checkExistingAddress != null) {
            return "Address already exists at ID " + checkExistingAddress.getIdAddress();
        }
        else {
            address.setCompany(companyRepository.findCompanyByCompanyId(idCompany));
            address = saveAddress(address);
        }
        return "Successfully created new Address named: " + address.getAddressName() + ", at ID: " + address.getIdAddress();
    }

    public ArrayList<String> getModifiedAddressErrors(Errors errors) {
        ArrayList<String> res = new ArrayList<>();

        errors.getFieldErrors().forEach(error -> res.add(error.getDefaultMessage()));

        return res;
    }

    public String deleteAddressById(Integer id) {
        Address address = addressRepository.findById(id).orElse(null);

        if (address != null) {
            addressRepository.deleteById(id);
        }
        else {
            return "No such existing Address!";
        }
        return "Successfully deleted Address named: " + address.getAddressName();
    }

    public ArrayList<String> getModifiedContactErrors(Errors errors) {
        ArrayList<String> res = new ArrayList<>();

        errors.getFieldErrors().forEach(error -> res.add(error.getDefaultMessage()));

        return res;
    }
    public String addNewContact(Contact contact, Integer idAddress) {
        Contact check = contactRepository.findByContactNameAndAddress(contact.getContactName(), idAddress);

        if (check == null) {
            contact.setAddress(addressRepository.findByIdAddress(idAddress));
            contactRepository.save(contact);
        }
        else {
            return "Contact with name: " + contact.getContactName() + " already exists in Address: " + addressRepository.findByIdAddress(idAddress).getAddressName();
        }

        return "Successfully created Contact named: " + contact.getContactName();
    }

    public String editContact(Contact contact, Integer idAddress) {
        Contact check = contactRepository.findByContactNameAndAddress(contact.getContactName(), idAddress);

        if (check != null) {
            check.setContactName(contact.getContactName());
            check.setEmail(contact.getEmail());
            check.setPosition(contact.getPosition());
            check.setPhone1(contact.getPhone1());
            check.setPhone2(contact.getPhone2());
            check.setBankAccount(contact.getBankAccount());

            contactRepository.save(check);
        }
        else {
            return "No such existing Contact named: " + contact.getContactName();
        }

        return "Successfully edited contact named: " + contact.getContactName() + " of Address: " + addressRepository.findByIdAddress(idAddress).getAddressName();
    }

    public String deleteContactById(Integer idContact) {
        Contact check = contactRepository.findById(idContact).orElse(null);

        if (check != null) {
            contactRepository.deleteById(idContact);
        }
        else {
            return "No such existing Contact";
        }
        return "Successfully deleted Contact named: " + check.getContactName();
    }

    public List<Company> findCompaniesByCompanyType(String companyType) {
        return companyRepository.findByCompanyType(companyType);
    }

    public List<Company> findCompaniesByCompanyType(String companyType1, String companyType2) {
        return companyRepository.findByCompanyType(companyType1, companyType2);
    }
    public List<Address> findAddressByCompanyType(String companyType) {
        return addressRepository.findByCompanyType(companyType);
    }

    public List<Address> findAddressByCompanyType(String companyType1, String companyType2) {
        return addressRepository.findByCompanyType(companyType1, companyType2);
    }
    public List<Contact> findContactsByCompanyTypeAndAddressType(String companyType, String addressType) {
        return contactRepository.findByCompanyTypeAndAddressType(companyType, addressType);
    }

    public Company findCompanyByIdCompany(List<String> res, String idCompany) {

        Company company = companyRepository.findById(Integer.parseInt(idCompany)).orElse(null);

        if (company == null) {
            res.add("Company with Id: " + idCompany + " does not exist anymore!");
        }

        return company;
    }

    public Address findAddressByIdAddress(List<String> res, String idAddress) {

        Address address = addressRepository.findById(Integer.parseInt(idAddress)).orElse(null);
        if (address == null) {
            res.add("Address with Id: " + idAddress + " does not exist anymore!");
        }
        else {
            res.add("Successfully found Address named: " + address.getAddressName() + " from Id: " + idAddress);
        }

        return address;
    }

    public Contact findContactByIdContact(List<String> res, String idContact) {

        Contact contact = contactRepository.findById(Integer.parseInt(idContact)).orElse(null);
        if (contact == null) {
            res.add("Contact with Id: " + idContact + " does not exist anymore!");
        }
        else {
            res.add("Successfully found Contact named: " + contact.getContactName() + " from Id: " + idContact);
        }
        return contact;
    }

    public Contact findContactByContactNameAndAddressName(List<String> results, String contactName, String addressName) {

        Contact contact = contactRepository.findByContactNameAndAddressName(contactName, addressName);
        if (contact == null) {
            results.add("Could not find Contact with contactName: " + contactName + " AND addressName: " + addressName);
        }
        return contact;
    }

    public List<Contact> findContactsByCompanyType(String companyType) {

        return contactRepository.findByCompanyType(companyType);
    }

    public Address findHQAddressByCompanyNameEn(String companyNameEn) {
        return addressRepository.findHQAddressByCompanyNameEn(companyNameEn);
    }

    public Address findAddressByAddressName(String addressName) {
        return addressRepository.findByAddressName(addressName);
    }

    public Contact findLandLineContactByHQAddressName(String hqAddressName) {
        return contactRepository.findLandLineContactByHQAddressName(hqAddressName);
    }

    public Contact findContactByIdContact(String idContact) {
        return contactRepository.findById(Integer.parseInt(idContact)).orElse(null);
    }
}
