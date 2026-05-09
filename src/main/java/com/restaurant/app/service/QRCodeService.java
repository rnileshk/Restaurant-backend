package com.restaurant.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    public Object generateQRCode(
            String text
    ) throws Exception {

        QRCodeWriter writer =
                new QRCodeWriter();

        return writer.encode(
                text,
                BarcodeFormat.QR_CODE,
                300,
                300
        );
    }
}