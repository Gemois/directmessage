package com.gmoi.directmessage.auth.tfa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {
    public String generateQRCode(String qrCodeText) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 250, 250);

        BufferedImage bufferedImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 250; x++) {
            for (int y = 0; y < 250; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
