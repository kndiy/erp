package com.kndiy.erp.controllers;

import com.kndiy.erp.others.MismatchedUnitException;
import com.kndiy.erp.services.ErrorHandlingService;
import com.kndiy.erp.services.SalesReportPrintingService;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoWrapper;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class SalesReportPrintingController {

    @Autowired
    private SalesReportPrintingService salesReportPrintingService;
    @Autowired
    private ErrorHandlingService errorHandlingService;

    @GetMapping(value = {
            "/sales-reporting/{dateRestriction}/",
            "/sales-reporting/{dateRestriction}"
    })
    public String showSalesReports(Model model, @PathVariable(name = "dateRestriction") String dateRestriction) {

        String[] dateArr = dateRestriction.replace("From_", "").split("_To_");
        String fromDateString = dateArr[0];
        String toDateString = dateArr[1];

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = LocalDate.parse(fromDateString, dtf);
        LocalDate toDate = LocalDate.parse(toDateString, dtf);

        SaleDeliveryDtoWrapper saleDeliveryDtoWrapper = (SaleDeliveryDtoWrapper) model.asMap().get("saleDeliveryDtoWrapper");

        if (saleDeliveryDtoWrapper == null) {
            saleDeliveryDtoWrapper = salesReportPrintingService.makeSaleDeliveryDtoWrapperFromDateToDate(fromDate, toDate);
            model.addAttribute("saleDeliveryDtoWrapper", saleDeliveryDtoWrapper);
        }

        String reportName = (String) model.asMap().get("reportName");

        model.addAttribute("reportName", reportName);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "reporting/sales-reporting";
    }

    @PostMapping("sales-reporting/{dateRestriction}/check-wrapper")
    public String checkWrapperDeliveryNote(@Valid @ModelAttribute SaleDeliveryDtoWrapper saleDeliveryDtoWrapper, Errors errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", errorHandlingService.parseError(errors));
            redirectAttributes.addFlashAttribute("errorType", "Printing Note");
        }
        else {
            redirectAttributes.addFlashAttribute("saleDeliveryDtoWrapper", saleDeliveryDtoWrapper);
            redirectAttributes.addFlashAttribute("reportName", saleDeliveryDtoWrapper.getReportName());
        }
        return "redirect:/sales-reporting/{dateRestriction}/";
    }


    @PostMapping("/sales-reporting/{dateRestriction}/print-note")
    public void printDeliveryNote(HttpServletResponse response, @ModelAttribute SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws IOException, MismatchedUnitException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate deliveryDate = saleDeliveryDtoWrapper.getDeliveryDate();
        Integer deliveryTurn = saleDeliveryDtoWrapper.getDeliveryTurn();

        String reportName = saleDeliveryDtoWrapper.getReportName();
        StringBuilder fileName = new StringBuilder(dtf.format(deliveryDate));

        switch (reportName) {
            case "delivery-note" -> fileName.append("_Turn").append(deliveryTurn).append("_DeliveryNote.pdf");
            case "delivery-label" -> fileName.append("_Turn").append(deliveryTurn).append("_DeliveryLabels.pdf");
            case "account-settling-note" -> fileName.append("_SettlingNote.pdf");
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition","inline; filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        OutputStream outputStream = response.getOutputStream();
        salesReportPrintingService.serveNote(outputStream, saleDeliveryDtoWrapper);

        response.flushBuffer();
    }

    @PostMapping("/sales-reporting/{dateRestriction}/print-both")
    public void printAccountSettlingNote(HttpServletResponse response, @ModelAttribute SaleDeliveryDtoWrapper saleDeliveryDtoWrapper) throws IOException, MismatchedUnitException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDate deliveryDate = saleDeliveryDtoWrapper.getDeliveryDate();

        String fileName = dtf.format(deliveryDate) + "_DeliveryNoteAndLabels.zip";

        response.setContentType("application/zip");
        response.setHeader("Content-disposition","inline; filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        OutputStream outputStream = response.getOutputStream();
        salesReportPrintingService.serveNote(outputStream, saleDeliveryDtoWrapper);

        response.flushBuffer();
    }
}
