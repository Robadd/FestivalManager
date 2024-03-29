package de.robadd.festivalmanager.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.zxing.WriterException;

import de.robadd.festivalmanager.Main;
import de.robadd.festivalmanager.model.Ticket;
import de.robadd.festivalmanager.model.TicketType;

public final class PDFWriter
{
    private static final Logger LOG = LoggerFactory.getLogger(PDFWriter.class);
    private static final String PNG = ".png";
    private static final String HTML = ".html";
    private static final String PDF = ".pdf";

    private PDFWriter()
    {
    }

    public static void preparePrint()
    {
        try
        {
            String savePath = Config.getInstance().getSavePath();
            final File backgroundImage = new File(savePath + "logo-big.jpg");
            FileUtils.copyURLToFile(Main.class.getResource("/logo-big.jpg"), backgroundImage);
        }
        catch (IOException e1)
        {
            LOG.error("", e1);
        }

    }

    public static void endPrint()
    {
        try
        {
            String savePath = Config.getInstance().getSavePath();
            final File backgroundImage = new File(savePath + "logo-big.jpg");
            Files.delete(backgroundImage.toPath());
        }
        catch (IOException e1)
        {
            LOG.error("", e1);
        }
    }

    public static File writePdf(final String savePath, final Ticket ticket, final Integer year)
    {
        File file = null;
        try
        {
            final String fileSafeName = ticket.getName().replace(' ', '_');
            final File qrCode = QRCodeGenerator.generateQrCode(savePath, fileSafeName, ticket);
            final String html = parseThymeleafTemplate(savePath + fileSafeName, ticket.getType(), year);

            final File htmlFile = new File(savePath + fileSafeName + HTML);
            try (FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(htmlFile, Charset.defaultCharset()))
            {
                fileWriter.write(html);
                fileWriter.flush();
            }

            final String htmlPathParameter = savePath + fileSafeName + HTML;
            final String pdfPathParameter = savePath + fileSafeName + PDF;
            final String command = MessageFormat.format(
                "{0} --headless --print-to-pdf-no-header --print-to-pdf=\"{1}\" \"file:///{2}\"",
                Config.getInstance().getChromePath(), pdfPathParameter, htmlPathParameter);
            final Process exec = Runtime.getRuntime().exec(command);
            exec.waitFor();
            Files.delete(htmlFile.toPath());
            Files.delete(qrCode.toPath());
            file = new File(savePath + fileSafeName + PDF);

        }
        catch (WriterException | IOException e)
        {
            LOG.error("Could not write file", e);
        }
        catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.info("PDF writer was interrupted", e);
        }
        return file;
    }

    private static String parseThymeleafTemplate(final String absoluteFilePath, final Integer type, final Integer year)
    {
        final TicketType ticketType = TicketType.forIdAndYear(type, year);
        if (ticketType == null)
        {
            return null;
        }
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(HTML);
        templateResolver.setTemplateMode(TemplateMode.HTML);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        final Context context = new Context();
        context.setVariable("year", year);
        context.setVariable("qr", absoluteFilePath + PNG);

        context.setVariable("type", ticketType.getName());
        context.setVariable("price", new DecimalFormat("#,00").format(ticketType.getPrice()));

        return templateEngine.process("template", context);
    }

}
