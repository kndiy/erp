package com.kndiy.erp.services.sales;

import com.kndiy.erp.dto.SaleDto;
import com.kndiy.erp.entities.companyCluster.Company;
import com.kndiy.erp.entities.companyCluster.Contact;
import com.kndiy.erp.entities.salesCluster.Sale;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.kndiy.erp.repositories.ContactRepository;
import com.kndiy.erp.services.inventory.InventoryOutService;
import com.kndiy.erp.repositories.CompanyRepository;
import com.kndiy.erp.repositories.SaleArticleRepository;
import com.kndiy.erp.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private SaleArticleRepository saleArticleRepository;
    @Autowired
    private InventoryOutService inventoryOutService;
    @Autowired
    private SaleLotService saleLotService;
    public List<Sale> findAllSales() {
        return saleRepository.findAll(Sort.by("orderDate").descending());
    }
    public Sale addNewSaleData(List<String> results, SaleDto saleDto) {

        Sale sale = saleRepository.findByOrderName(saleDto.getOrderName());

        if (sale != null) {
            results.add("Order with Name: " + saleDto.getOrderName() + " already exists at idSale: " + sale.getIdSale());
            return sale;
        }

        Company self = companyRepository.findCompanyByCompanyId(Integer.parseInt(saleDto.getIdCompanySource()));
        Company customer = companyRepository.findCompanyByCompanyId(Integer.parseInt(saleDto.getIdCustomer()));
        Contact contact = contactRepository.findById(Integer.parseInt(saleDto.getIdContactCustomer())).orElse(null);

        sale = new Sale();
        sale.setCompanySource(self);
        sale.setCustomer(customer);
        sale.setOrderPlacer(contact);
        sale.setNote(saleDto.getNote());
        sale.setDoneDelivery(false);
        sale.setOrderName(saleDto.getOrderName());
        sale.setOrderBatch(saleDto.getOrderBatch());
        sale.setOrderDate(saleDto.getOrderDate());

        results.add("Successfully added new Sale Data");
        return saleRepository.save(sale);
    }

    public Sale editSaleData(List<String> results, SaleDto saleDto) {

        Sale sale = saleRepository.findById(saleDto.getIdSale()).orElse(null);

        if (sale == null) {
            results.add("Sale data under Order named: " + saleDto.getOrderName() + " does not exist anymore!");
            return null;
        }

        Company self = companyRepository.findCompanyByCompanyId(Integer.parseInt(saleDto.getIdCompanySource()));
        Company customer = companyRepository.findCompanyByCompanyId(Integer.parseInt(saleDto.getIdCustomer()));
        Contact contact = contactRepository.findById(Integer.parseInt(saleDto.getIdContactCustomer())).orElse(null);

        sale.setOrderDate(saleDto.getOrderDate());
        sale.setOrderName(saleDto.getOrderName());
        sale.setNote(saleDto.getNote());
        sale.setCustomer(customer);
        sale.setOrderPlacer(contact);
        sale.setDoneDelivery(saleDto.getDoneDelivery());
        sale.setOrderBatch(saleDto.getOrderBatch());
        sale.setCompanySource(self);

        saleRepository.save(sale);

        results.add("Successfully edited Sale data with Order named: " + sale.getOrderName());
        return sale;
    }


    public boolean deleteSaleData(List<String> results, Integer idSale) {

        Sale sale = saleRepository.findById(idSale).orElse(null);

        if (sale == null) {
            results.add("Sale data with ID: " + idSale + " does not exist anymore!");
            return false;
        }

        List<SaleLot> saleLotList = saleLotService.findAllByIdSale(results, idSale);
        for (SaleLot saleLot : saleLotList) {
            saleLotService.deleteSaleLot(results, saleLot.getIdSaleLot());
        }

        saleRepository.delete(sale);
        results.add("Successfully deleted Sale data with ID: " + sale.getIdSale() + " under Order named: " + sale.getOrderName());
        return true;
    }

    public Sale findByIdSale(List<String> res, Integer idSale) {

        Sale sale = saleRepository.findById(idSale).orElse(null);

        if (sale == null) {
            res.add("Sale Data with id:" + idSale + " does not exist anymore!");
        }

        return sale;
    }

    public Sale findByOrderName(List<String> results, String orderName) {
        Sale sale = saleRepository.findByOrderName(orderName);

        if (sale == null) {
            results.add("Could not find Sale with OrderName: " + orderName);
        }

        return sale;
    }
}
