package com.kndiy.erp.pdfExpoter;

import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;


public class StampXOfYPageNumberFooter {

    private final Document document;
    private final PdfDocument pdfDocument;
    private final PdfFont font;
    private final float stampingHeight;

    public StampXOfYPageNumberFooter(Document document, PdfDocument pdfDocument, PdfFont font) {
        this.document = document;
        this.pdfDocument = pdfDocument;
        this.font = font;

        Paragraph paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                .setFont(font)
                .setFontSize(10)
                .add("Page");

        Cell cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(paragraph);

        Table table = new Table(new float[] {1})
                .useAllAvailableWidth()
                .setFixedLayout()
                .addCell(cell);

        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(document));

        LayoutResult result = renderer.layout((new LayoutContext(new LayoutArea(0, PageSize.A4))));
        stampingHeight = result.getOccupiedArea().getBBox().getHeight();
    }

    public void stamp() {

        final int totalPages = pdfDocument.getNumberOfPages();
        PageSize pageSize = pdfDocument.getDefaultPageSize();

        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

        float x = pageSize.getLeft() + document.getLeftMargin();

        //X of Y stamping occurs after the document was completed so, current bottom margin includes existing footer
        float y = pageSize.getBottom() + stampingHeight;
        Rectangle rectangle = new Rectangle(x, y, width, stampingHeight);

        for (int i = 1; i <= totalPages; i++) {

            PdfPage pdfPage = pdfDocument.getPage(i);
            PdfCanvas pdfCanvas = new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDocument);
            pdfCanvas.rectangle(rectangle);
            pdfCanvas.setStrokeColor(new DeviceRgb(255, 255, 255));
            pdfCanvas.stroke();

            Paragraph paragraph = ItextStaticConstructors.createParagraphSmallLeading()
                    .setFont(font)
                    .setFontSize(10)
                    .add(String.format("Page %s of %s", i, totalPages));

            Cell cell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBorderTop(new SolidBorder(0.5f))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .add(paragraph);

            Table table = new Table(new float[] {1})
                    .useAllAvailableWidth()
                    .setFixedLayout()
                    .addCell(cell);

            Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle)
                    .add(table);

            canvas.flush();
            canvas.close();
        }
    }

}
