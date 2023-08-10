package com.kndiy.erp.services.backupRestore;

import com.google.gson.Gson;
import com.kndiy.erp.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BackupService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemCodeRepository itemCodeRepository;
    @Autowired
    private ItemSellPriceRepository itemSellPriceRepository;
    @Autowired
    private ItemCodeSupplierRepository itemCodeSupplierRepository;
    @Autowired
    private ItemCodeSupplierEquivalentRepository itemCodeSupplierEquivalentRepository;
    @Autowired
    private InventoryInRepository inventoryInRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryOutRepository inventoryOutRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private SaleArticleRepository saleArticleRepository;
    @Autowired
    private SaleContainerRepository saleContainerRepository;
    @Autowired
    private SaleLotRepository saleLotRepository;

    private Map<String, Map<String, Map<String, String>>> entityMap;

    public void backup(BufferedWriter writer) throws IOException, IllegalAccessException {
        entityMap = new LinkedHashMap<>();

        backupEntity(companyRepository.findAll());
        backupEntity(addressRepository.findAll());
        backupEntity(contactRepository.findAll());

        backupEntity(itemCategoryRepository.findAll());
        backupEntity(itemTypeRepository.findAll());
        backupEntity(itemCodeRepository.findAll());
        backupEntity(itemSellPriceRepository.findAll());
        backupEntity(itemCodeSupplierRepository.findAll());
        backupEntity(itemCodeSupplierEquivalentRepository.findAll());

        backupEntity(inventoryInRepository.findAll());
        backupEntity(inventoryRepository.findAll());
        backupEntity(inventoryOutRepository.findAll());

        backupEntity(saleRepository.findAll());
        backupEntity(saleArticleRepository.findAll());
        backupEntity(saleContainerRepository.findAll());
        backupEntity(saleLotRepository.findAll());

        writer.write(new Gson().toJson(entityMap));
    }

    private <T> void backupEntity(List<T> list) throws IllegalAccessException {

        if (list.isEmpty()) {
            return;
        }

        String[] parseType = list.get(0).getClass().getTypeName().split("\\.");
        String[] typeWithHibernateProxy = parseType[parseType.length - 1].split("\\$");
        String tType = typeWithHibernateProxy[0];

        Map<String, Map<String, String>> entityMember = new HashMap<>();

        for (T member : list) {
            if (member instanceof HibernateProxy hibernateProxy) {
                LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
                member = (T) initializer.getImplementation();
            }

            parseEntityMember(entityMember, member, tType);
        }

        entityMap.put(tType,entityMember);
    }

    private <T> void parseEntityMember(Map<String, Map<String, String>> entityMember, T member, String tType) throws IllegalAccessException {

        String idName = "id" + tType;
        String id = "";
        Field[] fields = member.getClass().getDeclaredFields();

        Map<String, String> subMap = new HashMap<>();


        for (Field field : fields) {
            StringBuilder name = new StringBuilder(field.getName());
            Class<?> fieldType = field.getType();
            field.setAccessible(true);

            if (field.get(member) == null) {
                continue;
            }

            if (name.toString().equals(idName)) {
                id = field.get(member).toString();
            }
            else if (fieldType.isAssignableFrom(Integer.class) ||
                    fieldType.isAssignableFrom(String.class) ||
                    fieldType.isAssignableFrom(Float.class) ||
                    fieldType.isAssignableFrom(Double.class) ||
                    fieldType.isAssignableFrom(LocalDateTime.class) ||
                    fieldType.isAssignableFrom(LocalDate.class) ||
                    fieldType.isAssignableFrom(Boolean.class)) {

                subMap.put(name.toString(), field.get(member).toString());
            }
            else if (Collection.class.isAssignableFrom(fieldType)) {

                Collection<?> collection = (Collection<?>) field.get(member);
                StringBuilder concat = new StringBuilder();

                for (Object object : collection) {
                    concat.append(object.toString()).append(",");
                }
                subMap.put(name.toString(), concat.toString());
            }
            else {
                name.setCharAt(0,Character.toUpperCase(name.charAt(0)));
                subMap.put("id" + name.toString(), field.get(member).toString());
            }
        }
        entityMember.put(id, subMap);
    }
}
