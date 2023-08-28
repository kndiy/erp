package com.kndiy.erp.pdfExpoter;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.kndiy.erp.dto.InventoryOutDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryHeaderDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliverySummaryDtoWrapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class DeliveryNotePDFExporter {

    private TreeMap<List<String>, SaleDeliveryDtoWrapper> detailMap;
    private TreeMap<String, SaleDeliverySummaryDtoWrapper> summaryMap;
    private SaleDeliveryHeaderDto saleDeliveryHeaderDto;

    private final Color lightGrey;
    private final PdfFont regular;
    private final PdfFont bold;
    private final PdfFont italic;
    private final PdfFont boldItalic;

    public DeliveryNotePDFExporter() throws IOException {

        final int FONT_TYPES = 4;

        Resource[] fontResources = {
                new ClassPathResource("\\static\\fonts\\cambria.ttf"),
                new ClassPathResource("\\static\\fonts\\cambriab.ttf"),
                new ClassPathResource("\\static\\fonts\\cambriai.ttf"),
                new ClassPathResource("\\static\\fonts\\cambriaz.ttf")
        };

        PdfFont[] cambriaFonts = new PdfFont[FONT_TYPES];
        for (int i = 0; i < FONT_TYPES; i ++) {
            cambriaFonts[i] = PdfFontFactory.createFont(fontResources[i].getFile().getAbsolutePath(), PdfEncodings.IDENTITY_H, true);
        }
        this.regular = cambriaFonts[0];
        this.bold = cambriaFonts[1];
        this.italic = cambriaFonts[2];
        this.boldItalic = cambriaFonts[3];

        lightGrey = new DeviceRgb(235, 235, 235);
    }

    public void export(OutputStream responseOutputStream,
                       TreeMap<List<String>, SaleDeliveryDtoWrapper> detailMap,
                       TreeMap<String, SaleDeliverySummaryDtoWrapper> summaryMap,
                       SaleDeliveryHeaderDto saleDeliveryHeaderDto) throws IOException {

        this.detailMap = detailMap;
        this.summaryMap = summaryMap;
        this.saleDeliveryHeaderDto = saleDeliveryHeaderDto;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4, false);

        document.setMargins(10,10,10,10);

        makePdf(document, pdfDocument);
        document.flush();

        new StampXOfYPageNumberFooter(document, pdfDocument, boldItalic).stamp();
        document.flush();

        document.close();

        printFinalPdf(byteArrayOutputStream, responseOutputStream);
    }

    private void printFinalPdf(ByteArrayOutputStream byteArrayOutputStream, OutputStream responseOutputStream) throws IOException {

        byte[] bytes = byteArrayOutputStream.toByteArray();
        for (byte aByte : bytes) {
            responseOutputStream.write(aByte);
        }
    }

    private void makePdf(Document document, PdfDocument pdfDocument) throws IOException {

        Table headerTable = makeHeaderTable();
        TableHeaderEventHandler headerEventHandler = new TableHeaderEventHandler(document, headerTable);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, headerEventHandler);

        Table footerTable = makeFooterTable();
        TableFooterEventHandler footerEventHandler = new TableFooterEventHandler(document, footerTable);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerEventHandler);

        document.setMargins(10f + headerEventHandler.getTableHeight(), 10f, 5f + footerEventHandler.getTableHeight(), 10f);

        writeSummaryPages(document);

        writeLotPages(document);
    }


    private void writeSummaryPages(Document document) {

        for (Map.Entry<String, SaleDeliverySummaryDtoWrapper> entry : summaryMap.entrySet()) {

            //Leave some space after each table
            document.add(new Paragraph());

            Table table = makeSummaryTable(entry);
            document.add(table);
        }
    }

    private void writeLotPages(Document document) {

        for (Map.Entry<List<String>, SaleDeliveryDtoWrapper> entry : detailMap.entrySet()) {

            document.add(new AreaBreak());

            document.add(makeLotTableHeader(entry));

            for (SaleDeliveryDto dto : entry.getValue().getSaleDeliveryDtoList()) {
                document.add(makeLotTable(dto));
            }
        }
    }

    private Table makeLotTableHeader(Map.Entry<List<String>, SaleDeliveryDtoWrapper> entry) {

        Table lotTableHeader = new Table(new float[] {0.8f, 1.5f, 0.8f, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1.8f})
                .useAllAvailableWidth()
                .setFixedLayout()
                .setFont(regular)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER);

        fillLotTableHeaderSummaryPart(lotTableHeader, entry);
        fillLotTableHeader(lotTableHeader);

        return lotTableHeader;
    }

    private void fillLotTableHeaderSummaryPart(Table summaryTable, Map.Entry<List<String>, SaleDeliveryDtoWrapper> entry) {

        Cell cell;
        Paragraph paragraph;
        String itemType = entry.getKey().get(0);
        String orderName = entry.getKey().get(1);
        String itemCode = entry.getKey().get(2);
        String container = entry.getKey().get(3);
        String containerQuantity = entry.getValue().getContainerQuantity();
        String containerEquivalent = entry.getValue().getContainerEquivalent();
        String containerRolls = entry.getValue().getContainerRolls().toString();

        cell = new Cell(1,12).setBorder(Border.NO_BORDER).add("");
        summaryTable.addCell(cell);

        //Container Counts
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add("Count: " + containerRolls);
        cell = new Cell(1, 2).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph);
        summaryTable.addCell(cell);

        //Item Type
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(itemType);
        cell = new Cell(1,12).setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .add(paragraph);
        summaryTable.addCell(cell);

        //Container Quantity
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(containerQuantity);
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph);
        summaryTable.addCell(cell);

        //Order Name - LAST ROW START HERE
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(orderName);
        cell = new Cell(1,4).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(1)).add(paragraph);
        summaryTable.addCell(cell);

        //Item Code
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(itemCode);
        cell = new Cell(1, 5).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(1)).add(paragraph);
        summaryTable.addCell(cell);

        //Container
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(container);
        cell = new Cell(1, 3).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(1)).add(paragraph);
        summaryTable.addCell(cell);

        //Container Equivalent
        paragraph= ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(containerEquivalent);
        cell = new Cell(1,2).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setBorderBottom(new DashedBorder(1)).add(paragraph);
        summaryTable.addCell(cell);
    }

    private void fillLotTableHeader(Table headerTable) {

        Cell cell;
        Paragraph paragraph;

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add("LOT");
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(paragraph);
        headerTable.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add("STL/CLR");
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(paragraph);
        headerTable.addCell(cell);

        headerTable.addCell(new Cell(1,11).setBorder(Border.NO_BORDER).add(ItextStaticConstructors.createParagraphSmallLeading().add("")));

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add("TỔNG LOT");
        cell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).setFont(bold).add(paragraph);
        headerTable.addCell(cell);
    }

    private Table makeLotTable(SaleDeliveryDto dto) {

        Table lotTable = new Table(new float[] {0.8f, 1.5f, 0.8f, 10, 1.8f})
                .useAllAvailableWidth()
                .setFixedLayout()
                .setFont(regular)
                .setFontSize(9)
                .setBorder(Border.NO_BORDER);

        fillLotTable3FirstCells(lotTable, dto);

        Cell nestedContainer = new Cell().setBorder(Border.NO_BORDER).setPadding(0);
        nestedContainer.add(makeInventoryOutTable(dto.getInventoryOutDtoTreeSet()));
        lotTable.addCell(nestedContainer);

        fillLotTableLastCell(lotTable, dto);

        return lotTable;
    }

    private void fillLotTable3FirstCells(Table lotTable, SaleDeliveryDto dto) {

        Cell cell;
        Paragraph paragraph;

        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getLotName());
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).add(paragraph);
        lotTable.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphMediumLeading().setFont(regular).add(dto.getLotStyle());
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold).setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.LEFT).add(paragraph);
        paragraph = ItextStaticConstructors.createParagraphMediumLeading().setFont(regular).add(dto.getLotColor());
        cell.add(paragraph);
        lotTable.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add("Count");
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold).setVerticalAlignment(VerticalAlignment.TOP)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(paragraph);
        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getInventoryOutDtoTreeSet().first().getQuantity().split(" ")[1]);
        cell.add(paragraph);
        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getInventoryOutDtoTreeSet().first().getEquivalent().split(" ")[1]);
        cell.add(paragraph);
        lotTable.addCell(cell);
    }

    private void fillLotTableLastCell(Table lotTable, SaleDeliveryDto dto) {
        //Sum Lot
        Cell cell;
        Paragraph paragraph;

        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getLotRolls().toString());
        cell = new Cell().setBorder(Border.NO_BORDER).setFont(bold)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(paragraph);
        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getLotQuantity().split(" ")[0]);
        cell.add(paragraph);
        paragraph = ItextStaticConstructors.createParagraphMediumLeading().add(dto.getLotEquivalent().split(" ")[0]);
        cell.add(paragraph);
        lotTable.addCell(cell);
    }

    private Table makeInventoryOutTable(TreeSet<InventoryOutDto> inventoryOutDtoTreeSet) {

        Table inventoryOutTable = new Table(new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1})
                .setFixedLayout()
                .useAllAvailableWidth()
                .setFont(regular)
                .setFontSize(9);

        Cell cell;
        Paragraph paragraph;

        int i = 0;

        for (InventoryOutDto dto : inventoryOutDtoTreeSet) {

            paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                    .setPaddingRight(1)
                    .add(Integer.toString(i + 1));
            cell = new Cell().setVerticalAlignment(VerticalAlignment.TOP)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    .setPadding(0)
                    .setTextAlignment(TextAlignment.RIGHT).add(paragraph);
            paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                    .setPaddingRight(1)
                    .setBackgroundColor(lightGrey)
                    .setBorderTop(new SolidBorder(0.5f))
                    .setBorderBottom(new SolidBorder(0.5f))
                    .add(dto.getQuantity().split(" ")[0]);
            cell.add(paragraph);
            paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                    .setPaddingRight(1)
                    .add(dto.getEquivalent().split(" ")[0]);
            cell.add(paragraph);
            inventoryOutTable.addCell(cell);

            i ++;
        }

        i = i % 10;
        int fillerLength = 10 - i;
        cell = new Cell(1, fillerLength).setBorder(Border.NO_BORDER);
        inventoryOutTable.addCell(cell);

        return inventoryOutTable;
    }

    private Table makeSummaryTable(Map.Entry<String, SaleDeliverySummaryDtoWrapper> entry) {

        Table table = new Table(new float[] {4, 4, 2, 1, 2, 2})
                .setFixedLayout()
                .setFont(regular)
                .setFontSize(10)
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        fillSummaryTableSUMLine(table, entry);
        fillSummaryTableHeaderCells(table);
        fillSummaryTableData(table, entry.getValue().getSaleDeliverySummaryDtoTreeSet());

        return table;
    }
    private void fillSummaryTableSUMLine(Table table, Map.Entry<String, SaleDeliverySummaryDtoWrapper> entry) {

        String itemTypeString = entry.getKey();
        SaleDeliverySummaryDtoWrapper itemTypeSummary = entry.getValue();
        String deliveryQuantity = itemTypeSummary.getDeliveryQuantity();
        String deliveryEquivalent = itemTypeSummary.getDeliveryEquivalent();
        Integer deliveryRoll = itemTypeSummary.getDeliveryRolls();

        Cell cell;
        Paragraph paragraph;

        //Write SUM row
        cell = new Cell(1,3).setBorder(Border.NO_BORDER);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(itemTypeString);
        table.addCell(cell.add(paragraph));

        cell = new Cell().setFont(bold).setBorder(Border.NO_BORDER);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setTextAlignment(TextAlignment.CENTER).add(deliveryRoll.toString());
        table.addCell(cell.add(paragraph));

        cell = new Cell().setFont(bold).setBorder(Border.NO_BORDER);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(deliveryQuantity);
        table.addCell(cell.add(paragraph));

        cell = new Cell().setFont(bold).setBorder(Border.NO_BORDER);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(deliveryEquivalent);
        table.addCell(cell.add(paragraph));
    }
    private void fillSummaryTableHeaderCells(Table table) {

        Paragraph paragraph;

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add("Tên Đơn");
        table.addCell(paragraph);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add("Mã Hàng");
        table.addCell(paragraph);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setTextAlignment(TextAlignment.CENTER).add("Sử Dụng");
        table.addCell(paragraph);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setTextAlignment(TextAlignment.CENTER).add("Count");
        table.addCell(paragraph);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add("Sản Lượng");
        table.addCell(paragraph);
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add("Quy Đổi");
        table.addCell(paragraph);
    }
    private void fillSummaryTableData(Table table, TreeSet<SaleDeliverySummaryDto> saleDeliverySummaryDtoTreeSet) {

        int i = 0;
        Cell cell;
        Paragraph paragraph;

        table.setFont(regular);
        for (SaleDeliverySummaryDto dto : saleDeliverySummaryDtoTreeSet) {

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading();
            table.addCell(cell.add(paragraph.add(dto.getOrderName())));

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading();
            table.addCell(cell.add(paragraph.add(dto.getItemCodeString())));

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell.add(paragraph.add(dto.getContainer())));

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell.add(paragraph.add(dto.getContainerRolls().toString())));

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading();
            table.addCell(cell.add(paragraph.add(dto.getContainerQuantity())));

            cell = ItextStaticConstructors.createAlternateCellNoBorder(i);
            paragraph = ItextStaticConstructors.createParagraphSmallLeading();
            table.addCell(cell.add(paragraph.add(dto.getContainerEquivalent())));

            i++;
        }
    }

    private Table makeHeaderTable() throws IOException {

        float smallLeading = 0.8f;

        Table table = new Table(new float[] {1.5f, 1.5f, 2, 1, 3, 0.5f, 3}).useAllAvailableWidth();
        table.setFont(regular).setFontSize(10);

        //To enforce width ratio
        table.setFixedLayout();
        Cell cell;

        //Write Logo Image
        String logoPath = new ClassPathResource("\\static\\images\\ds.jpg").getFile().getAbsolutePath();
        ImageData logoData = ImageDataFactory.create(logoPath);
        Image logo = new Image(logoData);
        cell = new Cell(3, 1).add(logo.setAutoScale(true)).setBorder(Border.NO_BORDER);
        table.addCell(cell);

        //Write first row
        cell = new Cell(1, 6).setBorder(Border.NO_BORDER);
        cell.add(new Paragraph(saleDeliveryHeaderDto.getSaleSourceNameVn()).setFont(bold).setFontSize(20).setMultipliedLeading(smallLeading));
        cell.add(new Paragraph(saleDeliveryHeaderDto.getSaleSourceHQAddress() + "; Tel: " + saleDeliveryHeaderDto.getSaleSourceLandLine())
                .setFont(italic)
                .setFontSize(9)
                .setFirstLineIndent(20)
                .setMultipliedLeading(smallLeading));
        table.addCell(cell);

        //Write third row
        cell = new Cell(1, 1).setBorder(Border.NO_BORDER);
        cell.add(new Paragraph("Khách Hàng").setMultipliedLeading(smallLeading));
        table.addCell(cell);
        cell = new Cell(1, 5).setBorder(Border.NO_BORDER);
        cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getCustomerNameVn()).setFont(bold).setMultipliedLeading(smallLeading));
        table.addCell(cell);

        //write forth row
        cell = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Địa Chỉ").setMultipliedLeading(smallLeading));
        table.addCell(cell);
        cell = new Cell(1, 5).setBorder(Border.NO_BORDER);
        cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getCustomerHQAddress()).setFont(bold).setMultipliedLeading(smallLeading));
        table.addCell(cell);

        //Giao Tai Row
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph("Giao Tại").setMultipliedLeading(smallLeading)));
        cell = new Cell(1, 2).setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getDeliverToAddressName()).setFont(bold).setMultipliedLeading(smallLeading));
        table.addCell(cell);
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph("Liên Hệ").setMultipliedLeading(smallLeading)));
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getReceiverName()).setFont(bold).setMultipliedLeading(smallLeading)));
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph("ĐT").setMultipliedLeading(smallLeading)));
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getReceiverPhone()).setFont(bold).setMultipliedLeading(smallLeading)));

        //Địa Chỉ Giao row
        cell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph("Địa Chỉ Giao").setMultipliedLeading(smallLeading)));
        cell = new Cell(1,6).setBorder(Border.NO_BORDER).setBackgroundColor(lightGrey);
        table.addCell(cell.add(new Paragraph(": " + saleDeliveryHeaderDto.getDeliverToAddressVn()).setFont(bold).setMultipliedLeading(smallLeading)));

        //Title of DeliveryNote
        Text title = new Text("PHIẾU XUẤT KHO ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        String date = dtf.format(saleDeliveryHeaderDto.getDeliveryDate());
        String turn = " - Đợt " + saleDeliveryHeaderDto.getDeliveryTurn();

        Paragraph paragraph = new Paragraph().setFont(bold).setFontSize(14).setTextAlignment(TextAlignment.CENTER);
        paragraph.add(title).add(date).add(turn);


        cell = new Cell(1,7).setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell.add(paragraph));

        return table;
    }

    private Table makeFooterTable() {

        Paragraph paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(boldItalic)
                .setFontSize(10)
                .add("Phiếu Xuất Kho " + saleDeliveryHeaderDto.getDeliveryDate() + " - Đợt giao thứ: " + saleDeliveryHeaderDto.getDeliveryTurn());

        Cell cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .add(paragraph);

        return new Table(new float[] {1})
                .setFixedLayout()
                .useAllAvailableWidth()
                .addCell(cell);
    }

}
