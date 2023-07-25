package de.robadd.festivalmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.errorprone.annotations.CheckReturnValue;

public class ZipUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

    private ZipUtils()
    {
    }

    @CheckReturnValue
    public static File unzip(final File zippedFile)
    {
        File destDir = FileUtils.getTempDirectory();

        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zippedFile)))
        {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null)
            {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory())
                {
                    if (!newFile.isDirectory() && !newFile.mkdirs())
                    {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                }
                else
                {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs())
                    {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    writeToFile(buffer, zis, newFile);
                }
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
        }
        catch (IOException e)
        {
            LOG.error("Unable to unzip file {}", zippedFile, e);
            return null;
        }
        return destDir;

    }

    private static void writeToFile(final byte[] buffer, final ZipInputStream zis, final File newFile)
            throws IOException
    {
        // write file content
        try (FileOutputStream fos = new FileOutputStream(newFile);)
        {
            int len;
            while ((len = zis.read(buffer)) > 0)
            {
                fos.write(buffer, 0, len);
            }
        }
    }

    public static File newFile(final File destinationDir, final ZipEntry zipEntry) throws IOException
    {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator))
        {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
