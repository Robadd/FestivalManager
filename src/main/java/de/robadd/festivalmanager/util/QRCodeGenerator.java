package de.robadd.festivalmanager.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import de.robadd.festivalmanager.model.Ticket;

public final class QRCodeGenerator
{

    private QRCodeGenerator()
    {
    }

    public static File generateQrCode(final String savePath, final String name, final Ticket ticket)
            throws WriterException, IOException
    {
        final QRCodeWriter barcodeWriter = new QRCodeWriter();
        final BitMatrix bitMatrix = barcodeWriter.encode(
            Base64.getEncoder().encodeToString(ticket.toString().getBytes(StandardCharsets.UTF_8)),
            BarcodeFormat.QR_CODE,
            200, 200);
        final BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        final File outputfile = new File(savePath + name + ".png");
        ImageIO.write(bufferedImage, "png", outputfile);
        return outputfile;
    }

}
