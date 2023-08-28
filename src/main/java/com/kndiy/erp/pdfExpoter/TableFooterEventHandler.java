package com.kndiy.erp.pdfExpoter;

import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;
import lombok.Getter;

public class TableFooterEventHandler implements IEventHandler {

    private final Document document;
    private final Table footerTable;

    @Getter
    private final float tableHeight;
    public TableFooterEventHandler(Document document, Table footerTable) {

        this.document = document;
        this.footerTable = footerTable;

        TableRenderer renderer = (TableRenderer) footerTable.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(document));

        LayoutResult result = renderer.layout((new LayoutContext(new LayoutArea(0, PageSize.A4))));
        tableHeight = result.getOccupiedArea().getBBox().getHeight();
    }

    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        PdfPage pdfPage = pdfDocumentEvent.getPage();
        PdfCanvas pdfCanvas = new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDocument);
        PageSize pageSize = pdfDocument.getDefaultPageSize();

        float width = pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin();
        float height = getTableHeight();
        float x = pageSize.getX() + document.getLeftMargin();
        float y = pageSize.getBottom() + height;

        Rectangle rectangle = new Rectangle(x, y, width, height);

        pdfCanvas.rectangle(rectangle);
        pdfCanvas.setStrokeColor(new DeviceRgb(255,255,255));
        pdfCanvas.stroke();

        Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);
        canvas.add(footerTable);
        canvas.flush();
        canvas.close();
    }
}
