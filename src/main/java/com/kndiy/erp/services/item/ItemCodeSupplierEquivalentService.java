package com.kndiy.erp.services.item;

import com.kndiy.erp.dto.ItemCodeSupplierEquivalentDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.itemCodeCluster.ItemCode;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplier;
import com.kndiy.erp.entities.itemCodeCluster.ItemCodeSupplierEquivalent;
import com.kndiy.erp.repositories.ItemCodeSupplierEquivalentRepository;
import com.kndiy.erp.services.CompanyClusterService;
import com.kndiy.erp.wrapper.ItemCodeSupplierEquivalentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ItemCodeSupplierEquivalentService {

    @Autowired
    private ItemCodeSupplierEquivalentRepository itemCodeSupplierEquivalentRepository;
    @Autowired
    private ItemCodeService itemCodeService;
    @Autowired
    private CompanyClusterService companyClusterService;
    @Autowired
    private ItemCodeSupplierService itemCodeSupplierService;

    public List<String> addNewItemCodeSupplierEquivalent(ItemCodeSupplierEquivalentDto itemCodeSupplierEquivalentDto) {

        List<String> results = new ArrayList<>();

        String[] sourceUnitArr = itemCodeSupplierEquivalentDto.getSourceUnit().split(",");
        String[] equivalentUnitArr = itemCodeSupplierEquivalentDto.getEquivalentUnit().split(",");

        if (sourceUnitArr[0].equals("NewUnit")) {
            itemCodeSupplierEquivalentDto.setSourceUnit(sourceUnitArr[1].trim().toLowerCase());
        }
        else {
            itemCodeSupplierEquivalentDto.setSourceUnit(sourceUnitArr[0].trim().toLowerCase());
        }

        if (equivalentUnitArr[0].equals("NewUnit")) {
            itemCodeSupplierEquivalentDto.setEquivalentUnit(equivalentUnitArr[1].trim().toLowerCase());
        }
        else {
            itemCodeSupplierEquivalentDto.setEquivalentUnit(equivalentUnitArr[0].trim().toLowerCase());
        }

        List<Integer> idItemCodeSupplier = Arrays.stream(itemCodeSupplierEquivalentDto.getIdItemCodeSupplier().split("-")).map(Integer::parseInt).toList();

        ItemCode itemCode = itemCodeService.findByIdItemCode(results, idItemCodeSupplier.get(0));
        if (itemCode == null) {
            return results;
        }

        Company supplier = companyClusterService.findCompanyByIdCompany(results, idItemCodeSupplier.get(1).toString());
        if (supplier == null) {
            return results;
        }

        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierService.findByItemCodeAndSupplier(results, itemCode, supplier);
        if (itemCodeSupplier == null) {
            return results;
        }

        ItemCodeSupplierEquivalent itemCodeSupplierEquivalent = itemCodeSupplierEquivalentRepository.findByItemCodeSupplierAndUnits(itemCodeSupplier,
                itemCodeSupplierEquivalentDto.getSourceUnit(), itemCodeSupplierEquivalentDto.getEquivalentUnit());

        if (itemCodeSupplierEquivalent != null) {
            results.add("Equivalent from Unit: " + itemCodeSupplierEquivalent.getSourceUnit() +
                    " to Unit: " + itemCodeSupplierEquivalent.getEquivalentUnit() +
                    " for ItemCodeSupplier named: " + itemCodeSupplierEquivalent.getItemCodeSupplier().getItemCodeSupplierString() +
                    " already exists!");
            return results;
        }

        itemCodeSupplierEquivalent = new ItemCodeSupplierEquivalent();

        itemCodeSupplierEquivalent.setItemCodeSupplier(itemCodeSupplier);
        itemCodeSupplierEquivalent.setSourceUnit(itemCodeSupplierEquivalentDto.getSourceUnit());
        itemCodeSupplierEquivalent.setEquivalentUnit(itemCodeSupplierEquivalentDto.getEquivalentUnit());
        itemCodeSupplierEquivalent.setEquivalent(itemCodeSupplierEquivalentDto.getEquivalent());

        itemCodeSupplierEquivalentRepository.save(itemCodeSupplierEquivalent);

        results.add("Successfully added new ItemCodeSupplierEquivalent");

        log.info(results.toString());

        return results;
    }

    public Map<String, ItemCodeSupplierEquivalentWrapper> mapByIdItemCodeSupplier() {

        Map<String, ItemCodeSupplierEquivalentWrapper> map = new HashMap<>();

        List<ItemCodeSupplierEquivalent> list = itemCodeSupplierEquivalentRepository.findAll();

        list.forEach(item -> {

            String idItemCodeSupplier = item.getItemCodeSupplier().toString();

            ItemCodeSupplierEquivalentWrapper itemCodeSupplierEquivalentWrapper = map.get(idItemCodeSupplier);
            if (itemCodeSupplierEquivalentWrapper == null) {
                itemCodeSupplierEquivalentWrapper = new ItemCodeSupplierEquivalentWrapper(new ArrayList<>(List.of(item)));
                map.put(idItemCodeSupplier, itemCodeSupplierEquivalentWrapper);
                return;
            }

            List<ItemCodeSupplierEquivalent> itemCodeSupplierEquivalentList = itemCodeSupplierEquivalentWrapper.getItemCodeSupplierEquivalentList();
            if (itemCodeSupplierEquivalentList == null) {
                itemCodeSupplierEquivalentList = new ArrayList<>(List.of(item));
            }
            else {
                itemCodeSupplierEquivalentList.add(item);
            }
        });

        return map;
    }

    public TreeSet<String> findAllUnits() {

        TreeSet<String> units = new TreeSet<>();
        units.addAll(itemCodeSupplierEquivalentRepository.findEquivalentUnits());
        units.addAll(itemCodeSupplierEquivalentRepository.findSourceUnits());

        return units;
    }

    public List<ItemCodeSupplierEquivalent> findAllByItemCodeSupplier(List<String> results, ItemCodeSupplier itemCodeSupplier) {
        return itemCodeSupplierEquivalentRepository.findByItemCodeSupplier(itemCodeSupplier);
    }

    public List<String> editItemCodeSupplierEquivalent(ItemCodeSupplierEquivalentDto itemCodeSupplierEquivalentDto) {

        List<String> results = new ArrayList<>();


        String[] sourceUnitArr = itemCodeSupplierEquivalentDto.getSourceUnit().split(",");
        String[] equivalentUnitArr = itemCodeSupplierEquivalentDto.getEquivalentUnit().split(",");

        if (sourceUnitArr[0].equals("NewUnit")) {
            itemCodeSupplierEquivalentDto.setSourceUnit(sourceUnitArr[1].trim().toLowerCase());
        }
        else {
            itemCodeSupplierEquivalentDto.setSourceUnit(sourceUnitArr[0].trim().toLowerCase());
        }

        if (equivalentUnitArr[0].equals("NewUnit")) {
            itemCodeSupplierEquivalentDto.setEquivalentUnit(equivalentUnitArr[1].trim().toLowerCase());
        }
        else {
            itemCodeSupplierEquivalentDto.setEquivalentUnit(equivalentUnitArr[0].trim().toLowerCase());
        }


        ItemCodeSupplierEquivalent itemCodeSupplierEquivalent = findByIdItemCodeSupplierEquivalent(results, itemCodeSupplierEquivalentDto.getIdItemCodeSupplierEquivalent());

        if (itemCodeSupplierEquivalent == null) {
            return results;
        }

        ItemCodeSupplier itemCodeSupplier = itemCodeSupplierEquivalent.getItemCodeSupplier();

        ItemCodeSupplierEquivalent checkUnits = itemCodeSupplierEquivalentRepository.findByItemCodeSupplierAndUnits(itemCodeSupplier,
                itemCodeSupplierEquivalentDto.getSourceUnit(), itemCodeSupplierEquivalentDto.getEquivalentUnit());

        if (checkUnits != null &&
                !Objects.equals(checkUnits.getIdItemCodeSupplierEquivalent(), itemCodeSupplierEquivalent.getIdItemCodeSupplierEquivalent())) {
            results.add("Equivalent from Unit: " + checkUnits.getSourceUnit() +
                    " to Unit: " + checkUnits.getEquivalentUnit() +
                    " for ItemCodeSupplier named: " + checkUnits.getItemCodeSupplier().getItemCodeSupplierString() +
                    " already exists!");
            return results;
        }

        itemCodeSupplierEquivalent.setEquivalent(itemCodeSupplierEquivalentDto.getEquivalent());
        itemCodeSupplierEquivalent.setEquivalentUnit(itemCodeSupplierEquivalentDto.getEquivalentUnit());
        itemCodeSupplierEquivalent.setSourceUnit(itemCodeSupplierEquivalentDto.getSourceUnit());

        itemCodeSupplierEquivalentRepository.save(itemCodeSupplierEquivalent);

        results.add("Successfully edited Equivalent with id: " + itemCodeSupplierEquivalentDto.getIdItemCodeSupplierEquivalent());

        return results;
    }

    public List<String> deleteEquivalent(Integer idItemCodeSupplierEquivalent) {
        List<String> results = new ArrayList<>();

        ItemCodeSupplierEquivalent itemCodeSupplierEquivalent = findByIdItemCodeSupplierEquivalent(results, idItemCodeSupplierEquivalent);

        if (itemCodeSupplierEquivalent == null) {
            return results;
        }

        itemCodeSupplierEquivalentRepository.delete(itemCodeSupplierEquivalent);
        results.add("Successfully deleted Equivalent with Id: " + idItemCodeSupplierEquivalent);

        return results;
    }

    public ItemCodeSupplierEquivalent findByIdItemCodeSupplierEquivalent(List<String> results, Integer idItemCodeSupplierEquivalent) {

        ItemCodeSupplierEquivalent itemCodeSupplierEquivalent = itemCodeSupplierEquivalentRepository.findById(idItemCodeSupplierEquivalent).orElse(null);

        if (itemCodeSupplierEquivalent == null) {
            results.add("ItemCodeSupplierEquivalent with Id: " + idItemCodeSupplierEquivalent + " does not exist!");
        }

        return itemCodeSupplierEquivalent;

    }

    public String findEquivalentValueByItemCodeSupplierAndUnits(List<String> results, ItemCodeSupplier itemCodeSupplier, String inventoryUnit, String saleUnit) {

        ItemCodeSupplierEquivalent itemCodeSupplierEquivalent = itemCodeSupplierEquivalentRepository.findByItemCodeSupplierAndUnits(itemCodeSupplier, inventoryUnit, saleUnit);

        if (itemCodeSupplierEquivalent == null) {
            results.add("Could not find EquivalentValue for ItemCodeSupplier named: " + itemCodeSupplier.getItemCodeSupplierString() + " with SourceUnit: " + inventoryUnit + " and EquivalentUnit:" + saleUnit);
            return "1";
        }

        return itemCodeSupplierEquivalent.getEquivalent();
    }
}
