package com.kndiy.erp.pdfExpoter;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryHeaderDto;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoContainerWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoItemTypeWrapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DeliveryLabelPdfCreator {

    private final TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap;
    private final TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap;
    private final SaleDeliveryHeaderDto saleDeliveryHeaderDto;
    private final Color lightGrey;
    private final PdfFont regular;
    private final PdfFont bold;
    private final PdfFont italic;
    private final PdfFont boldItalic;
    private final String reportName;

    public DeliveryLabelPdfCreator(TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap,
                                  TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap,
                                  SaleDeliveryHeaderDto saleDeliveryHeaderDto) throws IOException {

        final int FONT_TYPES = 4;

        Resource[] fontResources = {
                new ClassPathResource("\\static\\fonts\\MyriadPro-Cond.ttf"),
                new ClassPathResource("\\static\\fonts\\MyriadPro-BoldCond.ttf"),
                new ClassPathResource("\\static\\fonts\\MyriadPro-CondIt.ttf"),
                new ClassPathResource("\\static\\fonts\\MyriadPro-BoldCondIt.ttf")
        };

        PdfFont[] myriadFonts = new PdfFont[FONT_TYPES];
        for (int i = 0; i < FONT_TYPES; i ++) {
            myriadFonts[i] = PdfFontFactory.createFont(fontResources[i].getFile().getAbsolutePath(), PdfEncodings.IDENTITY_H, true);
        }
        this.regular = myriadFonts[0];
        this.bold = myriadFonts[1];
        this.italic = myriadFonts[2];
        this.boldItalic = myriadFonts[3];

        lightGrey = ItextStaticConstructors.lightGrey;
        reportName = "Nhãn Hàng " + saleDeliveryHeaderDto.getDeliveryDate() + " - Đợt giao thứ: " + saleDeliveryHeaderDto.getDeliveryTurn();

        this.containerMap = containerMap;
        this.itemTypeMap = itemTypeMap;
        this.saleDeliveryHeaderDto = saleDeliveryHeaderDto;
    }

    public byte[] create() throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4, false);

        document.setMargins(10,10,10,10);

        makePdf(document, pdfDocument);

        new StampXOfYPageNumberFooter(document, pdfDocument, boldItalic, reportName).stamp();
        document.flush();

        document.close();

        return byteArrayOutputStream.toByteArray();
    }



    private void makePdf(Document document, PdfDocument pdfDocument) throws IOException {

        document.setMargins(10f, 10f, 15f, 10f);

        Table headerTable = makeDummyHeaderTable();
        TableHeaderEventHandler headerEventHandler = new TableHeaderEventHandler(document, headerTable);
        pdfDocument.addEventHandler(PdfDocumentEvent.INSERT_PAGE, headerEventHandler);

        document.setMargins(10f + headerEventHandler.getTableHeight(), 10f, 15f, 10f);

        writeContents(document, headerEventHandler);
    }



    private void writeContents(Document document, TableHeaderEventHandler headerEventHandler) {

        int i = 0;

        for (Map.Entry<List<String>, SaleDeliveryDtoContainerWrapper> entry : containerMap.entrySet()) {

            Table summaryTable = makeSummaryTable(entry);
            headerEventHandler.setHeaderTable(summaryTable);

            if (i != 0) {
                document.add(new AreaBreak());
            }

            Table contents = makeContentTable(entry);
            document.add(contents);
            document.flush();

            i ++;
        }
    }

    private Table makeContentTable(Map.Entry<List<String>, SaleDeliveryDtoContainerWrapper> entry) {

        Table contentTable = new Table(new float[] {1,1})
                .setFixedLayout()
                .useAllAvailableWidth();

        SaleDeliveryDtoContainerWrapper wrapper = entry.getValue();
        List<String> containerInfo = entry.getKey();

        for (SaleDeliveryDto saleDeliveryDto : wrapper.getSaleDeliveryDtoList()) {

            int i = 1;

            for (InventoryOutDto inventoryOutDto : saleDeliveryDto.getInventoryOutDtoTreeSet()) {

                contentTable.addCell(makeInventoryOutCell(containerInfo, saleDeliveryDto, inventoryOutDto, i));
                contentTable.addCell(makeInventoryOutCell(containerInfo, saleDeliveryDto, inventoryOutDto, i));

                i ++;
            }
        }

        return contentTable;
    }

    private Cell makeInventoryOutCell(List<String> containerInfo, SaleDeliveryDto saleDeliveryDto, InventoryOutDto inventoryOutDto, int i) {

        SolidBorder border = new SolidBorder(Color.RED, 0.5f);

        Cell nestedTableContainer = new Cell()
                .setPadding(0)
                .setBorder(border);

        Table nestedTable = makeInventoryOutNestedTable(containerInfo, saleDeliveryDto, inventoryOutDto, i);

        nestedTableContainer.add(nestedTable);

        return nestedTableContainer;
    }

    private Table makeInventoryOutNestedTable(List<String> containerInfo, SaleDeliveryDto saleDeliveryDto, InventoryOutDto inventoryOutDto, int i) {

        Table table = new Table(new float[] {1,1,1,1,1,1,1,1,1,1})
                .useAllAvailableWidth()
                .setFixedLayout()
                .setKeepTogether(true)
                .setFont(bold)
                .setFontSize(11.2f);

        String itemTypeString = containerInfo.get(0);
        String orderName = containerInfo.get(1);
        String orderBatch = containerInfo.get(2);
        String itemCodeString = containerInfo.get(3);
        String container = containerInfo.get(4);

        Paragraph paragraph;
        Cell cell;

        //Nhà cung cấp - date - turn
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Nhà Cung Cấp");
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + saleDeliveryHeaderDto.getSaleSourceNameVn());
        cell = new Cell(1,4).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(" - " + saleDeliveryHeaderDto.getDeliveryDate().toString());
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add(" - Đợt: ");
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(saleDeliveryHeaderDto.getDeliveryTurn().toString());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //Tên Đơn, Đợt đơn
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Tên/ Đợt Đơn");
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + orderName);
        cell = new Cell(1,4).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(" - " + orderBatch);
        cell = new Cell(1,4).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //Tên hàng
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Loại Hàng");
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + itemTypeString);
        cell = new Cell(1,8).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //Container + code
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Phối");
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + container);
        cell = new Cell(1,3).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Mã");
        cell = new Cell(1,1).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + itemCodeString);
        cell = new Cell(1,4).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //lot + No. + equivalent
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add("Tên Lô");
        cell = new Cell(1,1).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + saleDeliveryDto.getLotName());
        cell = new Cell(1,1).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(regular).add(" - STT");
        cell = new Cell(1,1).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(": " + i);
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(inventoryOutDto.getEquivalent());
        cell = new Cell(1,5).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //production code + quantity + xuất xứ
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(inventoryOutDto.getProductionCode());
        Text text = new Text(" (" + saleDeliveryDto.getLotItemCodeSupplierString() + ")")
                .setFont(italic)
                .setFontSize(9);
        paragraph.add(text);
        cell = new Cell(1,5).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(inventoryOutDto.getQuantity());
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.RIGHT).add("XUẤT XỨ VIỆT NAM");
        cell = new Cell(1,3).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        return table;
    }

    private Table makeSummaryTable(Map.Entry<List<String>, SaleDeliveryDtoContainerWrapper> entry) {

        Table table = new Table(new float[] {1})
                .useAllAvailableWidth()
                .setFixedLayout();

        List<String> list = entry.getKey();

        StringBuilder info = new StringBuilder();

        for (int j = 0; j < list.size(); j ++) {
            info.append(list.get(j));
            if (j != list.size() - 1) {
                info.append(" --- ");
            }
        }

        Paragraph paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setFontSize(10).add(info.toString());
        Cell cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT)
                .setBorderBottom(new SolidBorder(Color.RED, 1))
                .add(paragraph);

        table.addCell(cell);
        return table;
    }

    private Table makeDummyHeaderTable() {
        Table table = new Table(new float[] {1})
                .useAllAvailableWidth()
                .setFixedLayout();

        Paragraph paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setFontSize(10).add("TempString");
        Cell cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT)
                .setBorderBottom(new SolidBorder(Color.RED, 1))
                .add(paragraph);

        table.addCell(cell);
        return table;
    }
}
