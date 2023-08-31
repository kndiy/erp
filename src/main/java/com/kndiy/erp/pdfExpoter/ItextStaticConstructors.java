package com.kndiy.erp.pdfExpoter;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import lombok.Getter;

@Getter
public class ItextStaticConstructors {

    private static final float smallLeading = 0.8f;
    private static final float mediumLeading = 1f;
    private static final float largeLeading = 1.1f;
    public static final Color lightGrey = new DeviceRgb(235, 236, 240);
    public static final Color brightGrey = new DeviceRgb(215,217,225);
    public static final Color yellow = new DeviceRgb(255, 235, 0);

    public static Cell createAlternateCellNoBorder(int i) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER);
        if (i % 2 == 1) {
            cell.setBackgroundColor(lightGrey);
        }
        return cell;
    }

    public static Paragraph createParagraphSmallLeading() {
        return new Paragraph().setMultipliedLeading(smallLeading);
    }

    public static Paragraph createParagraphMediumLeading() {
        return new Paragraph().setMultipliedLeading(mediumLeading);
    }

    public static Paragraph createParagraphLargeLeading() {
        return new Paragraph().setMultipliedLeading(largeLeading);
    }


}
