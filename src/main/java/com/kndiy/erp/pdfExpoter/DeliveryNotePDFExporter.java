package com.kndiy.erp.pdfExpoter;

import com.kndiy.erp.dto.CompanyInfoDto;
import com.kndiy.erp.dto.ContactInfoDto;
import com.kndiy.erp.entities.inventoryCluster.InventoryOut;
import com.kndiy.erp.entities.itemCodeCluster.ItemType;
import com.kndiy.erp.entities.salesCluster.SaleLot;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DeliveryNotePDFExporter {

    private List<SaleLot> saleLotList;
    private CompanyInfoDto customerInfo;
    private CompanyInfoDto saleSourceInfo;
    private ContactInfoDto receiverInfo;
    private Font font;
    private Font fontBold;
    private BaseFont baseFont;

    public DeliveryNotePDFExporter() {

        this.font = FontFactory.getFont("/fonts/cambria.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11f, Font.NORMAL, Color.BLACK);
        this.fontBold = FontFactory.getFont("/fonts/cambria.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 11f, Font.BOLD, Color.BLACK);
        baseFont = font.getBaseFont();


    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        fontBold.setSize(22f);

        Paragraph paragraph = new Paragraph(saleSourceInfo.getNameEn(), fontBold);
        document.add(paragraph);

        font.setSize(11f);
        paragraph = new Paragraph(saleSourceInfo.getHqAddress(), font);

        fontBold.setSize(11f);
        Phrase phrase1 = new Phrase("Khách Hàng\t: ", font);
        Phrase phrase2 = new Phrase(customerInfo.getNameEn(), fontBold);
        paragraph = new Paragraph();
        paragraph.add(phrase1);
        paragraph.add(phrase2);

        phrase1 = new Phrase("Địa Chỉ\t: ", font);
        phrase2 = new Phrase(customerInfo.getHqAddress(), fontBold);
        paragraph = new Paragraph();
        paragraph.add(phrase1);
        paragraph.add(phrase2);

    }


}
