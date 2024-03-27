package com.axonactive.dojo.employee.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Phaser;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private static final int totalPage = 1;

    @Override
    @SneakyThrows
    public void onEndPage(PdfWriter writer, Document document) {
        addHeader(writer, document);
        addFooter(writer, document);
    }

    private void addHeader(PdfWriter writer, Document document) throws BadElementException, IOException {
        PdfPTable header = new PdfPTable(2);
        Image logo = Image.getInstance(Objects.requireNonNull(getClass().getClassLoader().getResource("community.png")).toString());
        logo.scalePercent(25, 25);

        try {
            header.setWidthPercentage(100);
            header.setWidths(new int[]{85, 15});

            header.setTotalWidth(540);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(20);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setPaddingBottom(5);
            header.getDefaultCell().setBorderColor(BaseColor.GREEN);
            header.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
            header.addCell(new Phrase("Department management", new Font(Font.FontFamily.HELVETICA, 10)));

            PdfPCell pdfPCellLogo = new PdfPCell(logo);
            pdfPCellLogo.setBorder(Rectangle.BOTTOM);
            pdfPCellLogo.setBorderColor(BaseColor.RED);
            pdfPCellLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPCellLogo.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCellLogo.setPaddingBottom(5);

            header.addCell(pdfPCellLogo);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            header.writeSelectedRows(0, -1, 30, 830, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private void addFooter(PdfWriter writer, Document document) {
        PdfPTable footer = new PdfPTable(2);

        try {
            footer.setWidthPercentage(100);
            footer.setWidths(new int[]{85, 15});

            footer.setTotalWidth(540);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(20);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.GREEN);
            footer.addCell(new Phrase("© 2024 Tuan Khoa. All Rights Reserved", new Font(Font.FontFamily.HELVETICA, 10)));

            PdfPCell pageNumber = new PdfPCell(new Phrase(String.format("Page %d of %d", document.getPageNumber(), totalPage), new Font(Font.FontFamily.HELVETICA, 10)));
            pageNumber.setBorder(Rectangle.TOP);
            pageNumber.setBorderColor(BaseColor.RED);
            pageNumber.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(pageNumber);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 30, 40, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
}
