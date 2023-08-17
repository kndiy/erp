package com.kndiy.erp.pdfExpoter;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreatePdfTable {

    static private List<String> colNames;
    static private List<List<String>> colData;
    static private BaseFont baseFont;
    static private Font DEFAULT_FONT;
    static public PdfPTable createPdfTable(List<String> columnNames, float[] columnWidths, List<List<String>> columnData, Font font) {

        colNames = columnNames;
        colData = columnData;

        DEFAULT_FONT = font;
        baseFont = DEFAULT_FONT.getBaseFont();

        int colNum = columnNames.size();
        PdfPTable table = new PdfPTable(colNum);

        table.setWidthPercentage(100F);
        table.setWidths(columnWidths);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        return table;
    }

    private static void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.white);

        Font font = new Font(baseFont, 11f, Font.BOLD);
        for (String colName : colNames) {
            cell.setPhrase(new Phrase(colName, font));
            table.addCell(cell);
        }
    }

    private static void writeTableData(PdfPTable table) {
        for (List<String> list : colData) {
            for (String data : list) {
                table.addCell(data);
            }
        }
    }
}
