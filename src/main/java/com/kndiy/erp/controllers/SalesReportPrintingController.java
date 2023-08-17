package com.kndiy.erp.controllers;

import com.kndiy.erp.dto.SaleDeliveryDto;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import com.kndiy.erp.services.SalesReportPrintingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Controller
@Slf4j
public class SalesReportPrintingController {

    @Autowired
    private SalesReportPrintingService salesReportPrintingService;

    @GetMapping("/sales-reporting/{dateRestriction}/")
    public String showSalesReports(Model model, @PathVariable(name = "dateRestriction") String dateRestriction) {

        String[] dateArr = dateRestriction.replace("From_", "").split("_To_");
        String fromDateString = dateArr[0];
        String toDateString = dateArr[1];

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = LocalDate.parse(fromDateString, dtf);
        LocalDate toDate = LocalDate.parse(toDateString, dtf);

        TreeSet<SaleDeliveryDto> saleDeliveryDtoSet = salesReportPrintingService.findAllDeliveryDtoFromDateToDate(fromDate, toDate);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("saleDeliveryDto", saleDeliveryDtoSet);

        return "reporting/sales-reporting";
    }

}
