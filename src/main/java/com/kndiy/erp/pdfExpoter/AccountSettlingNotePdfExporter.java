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
import com.itextpdf.layout.property.TextAlignment;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliveryHeaderDto;
import com.kndiy.erp.dto.deliveryDto.SaleDeliverySummaryDto;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoContainerWrapper;
import com.kndiy.erp.wrapper.deliveryWrapper.SaleDeliveryDtoItemTypeWrapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class AccountSettlingNotePdfExporter {
    private final TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap;
    private final TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap;
    private final SaleDeliveryHeaderDto saleDeliveryHeaderDto;
    private final Color lightGrey;
    private final Color brightGrey;
    private final Color yellow;
    private final PdfFont regular;
    private final PdfFont bold;
    private final PdfFont italic;
    private final PdfFont boldItalic;
    private final String reportName;
    private final float[] defaultColumnWidths;
    private final float[] nestedLotTableColumnWidths;

    public AccountSettlingNotePdfExporter(TreeMap<List<String>, SaleDeliveryDtoContainerWrapper> containerMap,
                                          TreeMap<String, SaleDeliveryDtoItemTypeWrapper> itemTypeMap,
                                          SaleDeliveryHeaderDto saleDeliveryHeaderDto) throws IOException {

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

        lightGrey = ItextStaticConstructors.lightGrey;
        brightGrey = ItextStaticConstructors.brightGrey;
        yellow = ItextStaticConstructors.yellow;

        this.containerMap = containerMap;
        this.itemTypeMap = itemTypeMap;
        this.saleDeliveryHeaderDto = saleDeliveryHeaderDto;

        reportName = "Bảng Kê Công Nợ " + saleDeliveryHeaderDto.getDeliveryDate();
        defaultColumnWidths = new float[] {0.2f, 0.2f, 0.2f, 1.5f, 2, 1.5f, 0.8f, 2, 2, 2.5f};
        nestedLotTableColumnWidths = new float[] {1.5f, 2, 1.5f, 0.8f, 2, 2, 2.5f};
    }

    public void export(OutputStream responseOutputStream) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4, false);

        document.setMargins(10,10,10,10);

        makePdf(document, pdfDocument);
        document.flush();

        new StampXOfYPageNumberFooter(document, pdfDocument, boldItalic, reportName).stamp();
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

        document.setMargins(10f + headerEventHandler.getTableHeight(), 10f, 15, 10f);

        writeSumByDatePart(document);

        writeContentsPart(document);
    }

    private void writeSumByDatePart(Document document) {

        Paragraph paragraph;
        Cell cell;
        //1.indents, 2.lot, 3.stl/clr, 4.quy đổi, 5.đếm, 6.SL thực giao, 7.SL công nợ, 8.Thành tiền
        Table table = new Table(defaultColumnWidths)
                .setFixedLayout()
                .setMargin(0)
                .useAllAvailableWidth()
                .setFont(regular)
                .setFontSize(9);

        //AMOUNT WITH VAT
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add("TỔNG KÈM VAT");
        cell = new Cell(1,8)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(saleDeliveryHeaderDto.getDeliveryAmountWithVat());
        cell = new Cell(1,2)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        //VAT
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add("VAT");
        cell = new Cell(1,8)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(saleDeliveryHeaderDto.getVat());
        cell = new Cell(1,2)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        //AMOUNT
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add("TỔNG GIÁ TRỊ GIAO TRONG NGÀY");
        cell = new Cell(1,8)
                .setBackgroundColor(yellow)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(saleDeliveryHeaderDto.getDeliveryAmount());
        cell = new Cell(1,2)
                .setBackgroundColor(yellow)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        document.add(table);

        for (Map.Entry<String, SaleDeliveryDtoItemTypeWrapper> entry : itemTypeMap.entrySet()) {
            document.add(makeSumItemTypeTable(entry));
        }
    }

    private void writeContentsPart(Document document) {

        for (Map.Entry<String, SaleDeliveryDtoItemTypeWrapper> entry : itemTypeMap.entrySet()) {

            document.add(new Paragraph());
            document.add(new Paragraph());
            document.add(new Paragraph());

            String itemTypeString = entry.getKey();
            document.add(makeItemTypeHeaderTable(itemTypeString));

            TreeSet<SaleDeliverySummaryDto> saleDeliverySummaryDtoSet = entry.getValue().getSaleDeliverySummaryDtoTreeSet();
            for (SaleDeliverySummaryDto dto : saleDeliverySummaryDtoSet) {

                String orderName = dto.getOrderName();
                String orderBatch = dto.getOrderBatch();
                String itemCodeString = dto.getItemCodeString();
                String container = dto.getContainer();

                List<String> key = List.of(itemTypeString, orderName, orderBatch, itemCodeString, container);
                SaleDeliveryDtoContainerWrapper containerWrapper = containerMap.get(key);

                document.add(makeContainerTable(key, containerWrapper));

            }
        }
    }

    private Table makeItemTypeHeaderTable(String itemTypeString) {

        Paragraph paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setFontSize(9)
                .add(itemTypeString);
        Cell cell = new Cell().setBorder(Border.NO_BORDER)
                .setBorderTop(new SolidBorder(0.8f))
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        return new Table(new float[] {1}).useAllAvailableWidth().setFixedLayout().addCell(cell);
    }

    private Table makeSumItemTypeTable(Map.Entry<String, SaleDeliveryDtoItemTypeWrapper> entry) {

        Paragraph paragraph;
        Cell cell;
        //1.indents, 2.lot, 3.stl/clr, 4.quy đổi, 5.đếm, 6.SL thực giao, 7.SL công nợ, 8.Thành tiền
        Table table = new Table(defaultColumnWidths)
                .setFixedLayout()
                .setMargin(0)
                .useAllAvailableWidth()
                .setFont(regular)
                .setFontSize(9);

        String itemTypeString = entry.getKey();
        SaleDeliveryDtoItemTypeWrapper wrapper = entry.getValue();
        Integer itemTypeRolls = wrapper.getItemTypeRolls();
        String itemTypeEquivalent = wrapper.getItemTypeEquivalent();
        String itemTypeEquivalentAdjusted = wrapper.getItemTypeEquivalentAdjusted();
        String itemTypeAmount = wrapper.getItemTypeAmount();

        //ItemTypeString
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .add(itemTypeString);
        cell = new Cell(1,6)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        table.addCell(cell);

        //Count
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.CENTER)
                .add(itemTypeRolls.toString());
        cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        table.addCell(cell);

        //Equivalent
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(itemTypeEquivalent);
        cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        table.addCell(cell);

        //EquivalentAdjusted
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(itemTypeEquivalentAdjusted);
        cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        table.addCell(cell);

        //Amount
        paragraph = ItextStaticConstructors.createParagraphMediumLeading()
                .setFont(bold)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(itemTypeAmount);
        cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(brightGrey)
                .add(paragraph);
        table.addCell(cell);

        return table;
    }

    private Table makeContainerTable(List<String> key, SaleDeliveryDtoContainerWrapper containerWrapper) {

        //1.indents, 2.lot, 3.stl/clr, 4.quy đổi, 5.đếm, 6.SL thực giao, 7.SL công nợ, 8.Thành tiền
        Table containerTable = new Table(defaultColumnWidths)
                .setFixedLayout()
                .setMargin(0)
                .useAllAvailableWidth()
                .setFont(regular)
                .setFontSize(9);

        fillContainerTableHeader(containerTable, key, containerWrapper);

        for (SaleDeliveryDto saleDeliveryDto : containerWrapper.getSaleDeliveryDtoList()) {

            Cell indentation = new Cell(1,3).setBorder(Border.NO_BORDER);
            Cell nestedLotTableContainer = new Cell(1,7)
                    .setPadding(0)
                    .setBorder(Border.NO_BORDER)
                    .add(makeNestedLotTable(saleDeliveryDto));
            containerTable.addCell(indentation);
            containerTable.addCell(nestedLotTableContainer);
        }

        return containerTable;
    }

    private void fillContainerTableHeader(Table containerTable, List<String> key, SaleDeliveryDtoContainerWrapper containerWrapper) {

        String orderName = key.get(1);
        String orderBatch = key.get(2);
        String itemCodeString = key.get(3);
        String container = key.get(4);

        Paragraph paragraph;
        Cell cell;

        //1st row indentation - Top separate line
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFontColor(lightGrey).add("i");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //1st row OrderName - Top separate line
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold)
                .add(orderName + " - " + orderBatch + " - " + itemCodeString);
        cell = new Cell(1,9).setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row indentation
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFontColor(lightGrey).add("i");
        cell = new Cell(1, 2).setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row Container Name
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).add(container);
        cell = new Cell(1, 3).setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row quy đổi data
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).setTextAlignment(TextAlignment.RIGHT).add(containerWrapper.getContainerQuantity());
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row Count data
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).setTextAlignment(TextAlignment.CENTER).add(containerWrapper.getContainerRolls().toString());
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row SL thực data
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).setTextAlignment(TextAlignment.RIGHT).add(containerWrapper.getContainerEquivalent());
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row SL công nợ data
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).setTextAlignment(TextAlignment.RIGHT).add(containerWrapper.getContainerEquivalentAdjusted());
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //3rd row Thành tiền data
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(boldItalic).setTextAlignment(TextAlignment.RIGHT).add(containerWrapper.getContainerAmount());
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //2nd row indentation
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFontColor(lightGrey).add("i");
        cell = new Cell(1, 3).setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).add(paragraph);
        containerTable.addCell(cell);

        //2nd row Lot label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).add("Lot Name");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row Stl/Clr label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.CENTER).add("Stl/Clr");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row Quy Đổi label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.RIGHT).add("Quy Đổi");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row Count label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.CENTER).add("Count");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row SL Thực label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.RIGHT).add("SL Thực");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row SL Công Nợ label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.RIGHT).add("SL Công Nợ");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);

        //2nd row Thành Tiền label - dash bottom
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(italic).setTextAlignment(TextAlignment.RIGHT).add("Thành Tiền");
        cell = new Cell().setBackgroundColor(lightGrey).setBorder(Border.NO_BORDER).setBorderBottom(new DashedBorder(0.2f)).add(paragraph);
        containerTable.addCell(cell);
    }

    private Table makeNestedLotTable(SaleDeliveryDto saleDeliveryDto) {

        Paragraph paragraph;
        Cell cell;

        Table nestedTable = new Table(nestedLotTableColumnWidths)
                .setFixedLayout()
                .useAllAvailableWidth()
                .setFont(regular)
                .setFontSize(9);

        //1st row lot name
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add(saleDeliveryDto.getLotName());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row Stl/Clr
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.CENTER).add(saleDeliveryDto.getLotStyle() + " - " + saleDeliveryDto.getLotColor());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row quy đổi
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.RIGHT).add(saleDeliveryDto.getLotQuantity());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row count
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.CENTER).add(saleDeliveryDto.getLotRolls().toString());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row sl thực
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.RIGHT).add(saleDeliveryDto.getLotEquivalent());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row sl công nợ
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.RIGHT).add(saleDeliveryDto.getLotEquivalentAdjusted());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //1st row thành tiền
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setTextAlignment(TextAlignment.RIGHT).add(saleDeliveryDto.getLotAmount());
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //2nd row note
        paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(italic)
                .setFontSize(8)
                .add("Note: " + saleDeliveryDto.getLotNote());
        cell = new Cell(1,6).setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        //2nd row sellPrice
        paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(italic)
                .setFontSize(8)
                .add("(" + saleDeliveryDto.getItemSellPrice() + ")");
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        nestedTable.addCell(cell);

        return nestedTable;
    }

    private Table makeHeaderTable() throws IOException {

        float smallLeading = 0.8f;

        Table table = new Table(new float[] {1.5f, 1.5f, 2, 1, 3, 0.5f, 3})
                .useAllAvailableWidth()
                .setFont(regular).setFontSize(10);

        //To enforce width ratio
        table.setFixedLayout();
        Cell cell;
        Paragraph paragraph;

        //Write Logo Image
        String logoPath = new ClassPathResource("\\static\\images\\ds.jpg").getFile().getAbsolutePath();
        ImageData logoData = ImageDataFactory.create(logoPath);
        Image logo = new Image(logoData);
        cell = new Cell(3, 1).add(logo.setAutoScale(true)).setBorder(Border.NO_BORDER);
        table.addCell(cell);

        //Write first row
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).setFontSize(20).add(saleDeliveryHeaderDto.getSaleSourceNameVn());
        cell = new Cell(1, 6)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(italic)
                .setFontSize(9)
                .setFirstLineIndent(20)
                .add(saleDeliveryHeaderDto.getSaleSourceHQAddress() + "; Tel: " + saleDeliveryHeaderDto.getSaleSourceLandLine());
        cell.add(paragraph);
        table.addCell(cell);

        //Write third row
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add("Khách Hàng:");
        cell = new Cell(1, 1)
                .setBorder(Border.NO_BORDER)
                .add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading().setFont(bold).add(": " + saleDeliveryHeaderDto.getCustomerNameVn());
        cell = new Cell(1, 5).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        //write forth row
        paragraph = ItextStaticConstructors.createParagraphSmallLeading().add("Địa Chỉ");
        cell = new Cell().setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);

        paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(bold)
                .add(": " + saleDeliveryHeaderDto.getCustomerHQAddress());
        cell = new Cell(1, 5).setBorder(Border.NO_BORDER).add(paragraph);
        table.addCell(cell);


        //Title of Account Settling Note
        paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(bold)
                .setFontSize(13)
                .setTextAlignment(TextAlignment.CENTER)
                .add(reportName.toUpperCase());
        cell = new Cell(1,7).setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell.add(paragraph));

        return table;
    }
}
