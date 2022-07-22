package de.robadd.festivalmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.zxing.WriterException;

public final class PDFWriter
{
    private static final Logger LOG = LoggerFactory.getLogger(PDFWriter.class);
    private static final String PNG = ".png";
    private static final String HTML = ".html";
    private static final String PDF = ".pdf";

    private PDFWriter()
    {
    }

    public static File writePdf(final String savePath, final String string)
    {
        final String[] split2 = string.split(";");
        final String name = split2[0];
        final Integer type = Integer.valueOf(split2[1]);
        final Ticket ticket = new Ticket(name, type, Boolean.parseBoolean(split2[2]));

        return writePdf(savePath, ticket);
    }

    public static File writePdf(final String savePath, final Ticket ticket)
    {
        File file = null;
        try
        {
            final String fileSafeName = ticket.getName().replace(' ', '_');
            final File qrCode = QRCodeGenerator.generateQrCode(savePath, fileSafeName, ticket);
            final String html = parseThymeleafTemplate(savePath + fileSafeName, ticket.getType());

            final File htmlFile = new File(savePath + fileSafeName + HTML);
            try (FileWriter fileWriter = new FileWriter(htmlFile))
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

    private static String parseThymeleafTemplate(final String absoluteFilePath, final Integer type)
    {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(HTML);
        templateResolver.setTemplateMode(TemplateMode.HTML);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        final Context context = new Context();
        context.setVariable("qr", absoluteFilePath + PNG);
        context.setVariable("type", type == 1 ? "Tagesticket" : "Festivalpass (3 Tage)");
        context.setVariable("price", type == 1 ? "15,00" : "30,00");

        return templateEngine.process("template", context);
    }

}
