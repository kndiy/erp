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

public class TableHeaderEventHandler implements IEventHandler {

    private final Table table;
    @Getter
    private final float tableHeight;
    private final Document document;

    public TableHeaderEventHandler(Document document, Table table) {

        this.document = document;
        this.table = table;

        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(document));

        LayoutResult result = renderer.layout((new LayoutContext(new LayoutArea(0, PageSize.A4))));
        tableHeight = result.getOccupiedArea().getBBox().getHeight();
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        PdfPage page = documentEvent.getPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);
        PageSize pageSize = pdfDocument.getDefaultPageSize();

        float x = pageSize.getX() + document.getLeftMargin();
        float y = pageSize.getTop() - document.getTopMargin();
        float width = pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin();
        float height = getTableHeight();
        Rectangle rectangle = new Rectangle(x, y, width, height);

        pdfCanvas.rectangle(rectangle);
        pdfCanvas.setStrokeColor(new DeviceRgb(255,255,255));
        pdfCanvas.stroke();

        Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);
        canvas.add(table);
        canvas.flush();
        canvas.close();
    }

}
