package com.restaurant.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.restaurant.app.entity.TableBooking;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateBookingTicket(TableBooking booking) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
            Font subTitleFont = new Font(Font.HELVETICA, 13, Font.NORMAL);
            Font sectionFont = new Font(Font.HELVETICA, 15, Font.BOLD);
            Font footerFont = new Font(Font.HELVETICA, 11, Font.ITALIC);

            Paragraph title = new Paragraph("DineFlow Restaurant", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Booking Confirmation Ticket", subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph(" "));

            Paragraph section = new Paragraph("Booking Details", sectionFont);
            section.setSpacingBefore(10);
            section.setSpacingAfter(10);
            document.add(section);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 5});

            addRow(table, "Booking Code", booking.getBookingCode());
            addRow(table, "Customer Name", booking.getCustomerName());
            addRow(table, "Email", booking.getCustomerEmail());
            addRow(table, "Phone", booking.getCustomerPhone());
            addRow(table, "Persons", String.valueOf(booking.getTotalPersons()));
            addRow(table, "Booking Date", String.valueOf(booking.getBookingDate()));
            addRow(table, "Booking Time", String.valueOf(booking.getBookingTime()));
            addRow(table, "Special Request", booking.getSpecialRequest());
            addRow(table, "Checked In", String.valueOf(booking.getCheckedIn()));

            document.add(table);

            document.add(new Paragraph(" "));

            Paragraph qrTitle = new Paragraph("Scan QR For Booking Verification", sectionFont);
            qrTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(qrTitle);

            document.add(new Paragraph(" "));

            Image qrImage = generateQrImage(
                    "http://localhost:5173/verify-booking/" + booking.getBookingCode()
            );

            qrImage.scaleToFit(170, 170);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            document.add(new Paragraph(" "));

            Paragraph footer = new Paragraph(
                    "Please show this ticket during check-in.\nThank you for booking with us!",
                    footerFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate booking ticket PDF: " + e.getMessage());
        }
    }

    private Image generateQrImage(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        var bitMatrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                300,
                300
        );

        ByteArrayOutputStream qrOutput = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(
                bitMatrix,
                "PNG",
                qrOutput
        );

        return Image.getInstance(qrOutput.toByteArray());
    }

    private void addRow(PdfPTable table, String key, String value) {
        Font keyFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        PdfPCell keyCell = new PdfPCell(new Phrase(key, keyFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "-", valueFont));

        keyCell.setPadding(9);
        valueCell.setPadding(9);

        table.addCell(keyCell);
        table.addCell(valueCell);
    }
}