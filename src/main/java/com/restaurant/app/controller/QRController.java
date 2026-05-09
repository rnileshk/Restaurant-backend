package com.restaurant.app.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin("*")
public class QRController {

    @GetMapping(
            value = "/generate",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> generateQRCode(
            @RequestParam String text
    ) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            var bitMatrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    bitMatrix,
                    "PNG",
                    output
            );

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(output.toByteArray());

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }
}