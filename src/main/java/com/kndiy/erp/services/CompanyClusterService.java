package com.kndiy.erp.services;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.repositories.AddressRepository;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Comparator<Company> comp = Comparator.comparing(Company::getNameEn);
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

    public TreeSet<Address> getAddressesByCompanyID(Integer idCompany) {
        Comparator<Address> comp = Comparator.comparing(Address::getAddressType);
        TreeSet<Address> addresses = new TreeSet<>(comp);

        List<Address> addressList = addressRepository.findByCompanyId(idCompany);
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        addresses.addAll(addressList);

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

    public Company editCompany(List<String> results, Company company) {
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
            results.add("No such existing Company!");
            return null;
        }

        results.add("Successfully edited new Company named: " + curEditing.getNameEn());
        return curEditing;
    }

    public void deleteCompanyById(Integer idCompany) {

        companyRepository.deleteById(idCompany);
    }

    public TreeSet<Contact> getContactsByAddressId(Integer id) {
        Comparator<Contact> comp = Comparator.comparing(Contact::getContactName);
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
    public Address editAddress(List<String> results, Address address) {
        Address editingAddress = addressRepository.findById(address.getIdAddress()).orElse(null);

        if (editingAddress == null) {
            results.add("Could not find such existing Address");
            return null;
        }

        editingAddress.setAddressName(address.getAddressName());
        editingAddress.setTaxCode(address.getTaxCode());
        editingAddress.setAddressType(address.getAddressType());
        editingAddress.setAddressVn(address.getAddressVn());
        editingAddress.setAddressEn(address.getAddressEn());
        editingAddress.setDistance(address.getDistance());
        editingAddress.setRepresentative(address.getRepresentative());
        editingAddress.setOutsideCity(address.getOutsideCity());
        editingAddress = addressRepository.save(editingAddress);
        results.add("Successfully edited address named: " + editingAddress.getAddressName());

        return editingAddress;
    }

    public Address addNewAddress(List<String> results, Address address, Integer idCompany) {

        Address checkExistingAddress = addressRepository.findByAddressName(address.getAddressName());
        if (checkExistingAddress != null) {
            results.add("Address already exists at ID " + checkExistingAddress.getIdAddress());
            return checkExistingAddress;
        }

        address.setCompany(companyRepository.findCompanyByCompanyId(idCompany));
        address = saveAddress(address);

        results.add("Successfully created new Address named: " + address.getAddressName() + ", at ID: " + address.getIdAddress());
        return address;
    }

    public boolean deleteAddressById(List<String> results, Integer idAddress) {
        Address address = addressRepository.findById(idAddress).orElse(null);

        if (address != null) {
            addressRepository.deleteById(idAddress);
        }
        else {
            results.add("No such existing Address!");
            return false;
        }
        results.add("Successfully deleted Address named: " + address.getAddressName());
        return true;
    }

    public Contact addNewContact(List<String> results, Contact contact, Integer idAddress) {
        Contact check = contactRepository.findByContactNameAndAddress(contact.getContactName(), idAddress);

        if (check == null) {
            results.add("Successfully created Contact named: " + contact.getContactName());
            contact.setAddress(addressRepository.findByIdAddress(idAddress));
            return contactRepository.save(contact);
        }

        results.add("Contact with name: " + contact.getContactName() + " already exists in Address: " + addressRepository.findByIdAddress(idAddress).getAddressName());
        return check;
    }

    public Contact editContact(List<String> results, Contact contact, Integer idAddress) {
        Contact check = contactRepository.findByContactNameAndAddress(contact.getContactName(), idAddress);

        if (check != null) {
            check.setContactName(contact.getContactName());
            check.setEmail(contact.getEmail());
            check.setPosition(contact.getPosition());
            check.setPhone1(contact.getPhone1());
            check.setPhone2(contact.getPhone2());
            check.setBankAccount(contact.getBankAccount());
            results.add("Successfully edited contact named: " + contact.getContactName() + " of Address: " + addressRepository.findByIdAddress(idAddress).getAddressName());
            return contactRepository.save(check);
        }
        results.add("No such existing Contact named: " + contact.getContactName());
        return null;
    }

    public boolean deleteContactById(List<String> results, Integer idContact) {
        Contact check = contactRepository.findById(idContact).orElse(null);

        if (check != null) {
            contactRepository.deleteById(idContact);
        }
        else {
            results.add("No such existing Contact");
            return false;
        }
        results.add("Successfully deleted Contact named: " + check.getContactName());
        return true;
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

    public Company getCompanyById(Integer idCompany) {
        return companyRepository.findCompanyByCompanyId(idCompany);
    }

    public Address getAddressById(Integer idAddress) {
        return addressRepository.findByIdAddress(idAddress);
    }
}
