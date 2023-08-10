package com.kndiy.erp;

import com.kndiy.erp.entities.companyCluster.Address;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.services.CompanyClusterService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
public class TestPersistingCompanyCluster {
    @Autowired
    CompanyClusterService companyClusterService;
    private Map<Integer, Map<String, Object>> companyMap;
    private Map<Integer, Map<String, Object>> addressMap;
    private Map<Integer, Map<String, Object>> contactMap;
    private List<String> results;
    @Test
    public void test() throws IOException {

        results = new ArrayList<>();

        String companiesFilePath = "D:\\JAVA\\companies.json";
        String addressesFilePath = "D:\\JAVA\\addresses.json";
        String contactsFilePath = "D:\\JAVA\\contacts.json";
        File companiesFile = new File(companiesFilePath);
        File addressesFile = new File(addressesFilePath);
        File contactsFile = new File(contactsFilePath);

        companyMap = mapEntity(companiesFile, "Company");
        addressMap = mapEntity(addressesFile, "Address");
        contactMap = mapEntity(contactsFile, "Contact");

        addSavedEntities("Company");
        addSavedEntities("Address");
        addSavedEntities("Contact");

        log.info(results.toString());
    }
    private Map<Integer, Map<String, Object>> mapEntity(File file, String entityName) {
        Map<Integer, Map<String, Object>> entityMap = new HashMap<>();
        Gson gson = new Gson();
        String toGetId = "id" + entityName;

        try {
            JsonArray jsonArray = (JsonArray) JsonParser.parseReader(new FileReader(file));
            System.out.println("Read " + entityName + " file successfully");

            int n = jsonArray.size();
            for (int i = 0; i < n; i++) {
                JsonObject object = (JsonObject) jsonArray.get(i);
                Map<String, Object> entity = (Map<String, Object>) gson.fromJson(object, Map.class);
                int entityKey = object.get(toGetId).getAsInt();
                entityMap.put(entityKey, entity);
            }
        }
        catch (IOException ex) {
            System.out.println("Could not read " + entityName + " file");
        }

        return entityMap;
    }
    private void addSavedEntities(String entityName) {

        Iterator<Map.Entry<Integer, Map<String, Object>>> iter;
        switch (entityName) {
            case "Company" :
                iter = companyMap.entrySet().iterator();
                break;
            case "Address" :
                iter = addressMap.entrySet().iterator();
                break;
            default :
                iter = contactMap.entrySet().iterator();
                break;
        }
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<String, Object>> entry = iter.next();
            switch (entityName) {
                case "Company":
                    persistingCompany(entry.getValue());
                    break;
                case "Address" :
                    persistingAddress(entry.getValue());
                    break;
                default :
                    persistingContact(entry.getValue());
                    break;
            }
        }
    }
    private void persistingCompany(Map<String, Object> companyMap) {


        Company company = new Company();
        company.setNameEn((String) companyMap.get("nameEn"));
        company.setNameVn((String) companyMap.get("nameVn"));
        company.setCompanyType((String) companyMap.get("companyType"));
        company.setCompanyIndustry((String) companyMap.get("companyIndustry"));
        company.setAbbreviation((String) companyMap.get("abbreviation"));

        companyClusterService.addNewCompany(results, company);
    }
    private void persistingAddress(Map<String, Object> addressMap) {
        int idCompanyFromAddressMap = ((Double) addressMap.get("idCompany")).intValue();
        String companyNameEnOfAddress = (String) companyMap.get(idCompanyFromAddressMap).get("nameEn");

        Company company;
        if (companyNameEnOfAddress != null) {
            company = companyClusterService.findCompanyByCompanyNameEn(results, companyNameEnOfAddress);
        }
        else {
            String companyNameVnOfAddress = (String) companyMap.get(idCompanyFromAddressMap).get("nameVn");
            company = companyClusterService.findCompanyByCompanyNameVn(companyNameVnOfAddress);
        }

        Address address = new Address();
        address.setCompany(company);
        address.setAddressName((String) addressMap.get("addressName"));
        address.setAddressType((String) addressMap.get("addressType"));
        address.setAddressEn((String) addressMap.get("addressEn"));
        address.setAddressVn((String) addressMap.get("addressVn"));
        address.setRepresentative((String) addressMap.get("representative"));
        address.setTaxCode((String) addressMap.get("taxCode"));
        try {
            address.setDistance(Float.parseFloat((String) addressMap.get("distance")));
        }
        catch (Exception ex) {
            System.out.println("No value in distance field!");
        }
        address.setOutsideCity(Boolean.parseBoolean(String.valueOf(((Double) addressMap.get("outsideCity")).intValue())));

        String result = companyClusterService.addNewAddress(address, company.getIdCompany());
        results.add(result);
    }
    public void persistingContact(Map<String, Object> contactMap) {
        int idAddressFromContactMap = ((Double) contactMap.get("idAddress")).intValue();
        String addressName = (String) addressMap.get(idAddressFromContactMap).get("addressName");

        Address address = companyClusterService.findAddressByAddressName(results, addressName);
        Contact contact = new Contact();
        contact.setAddress(address);
        contact.setContactName((String) contactMap.get("contactName"));
        contact.setEmail((String) contactMap.get("email"));
        contact.setPosition((String) contactMap.get("position"));
        contact.setPhone1((String) contactMap.get("phone1"));
        contact.setPhone2((String) contactMap.get("phone2"));
        contact.setBankAccount((String) contactMap.get("bankAccount"));

        String result = companyClusterService.addNewContact(contact, address.getIdAddress());
        results.add(result);
    }
}
