package de.robadd.festivalmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.ui.MainWindow;
import de.robadd.festivalmanager.ui.UpdaterWindow;
import de.robadd.festivalmanager.updater.Updater;

public final class Main
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static boolean isDirty = false;

    /**
     * Constructor
     */
    private Main()
    {
    }

    public static void main(final String[] args) throws Exception
    {
        MainWindow.main();
        // showUpdateWindowIfNecessary();
    }

    private static boolean showUpdateWindowIfNecessary() throws IOException, ClassNotFoundException,
            InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException
    {
        boolean shouldShowMainWindow = false;
        try (InputStream inputStream = Main.class.getResourceAsStream("/version.properties"))
        {
            if (inputStream != null)
            {
                LOG.info("inputStream found");
                final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final BufferedReader reader = new BufferedReader(streamReader);
                for (String line; (line = reader.readLine()) != null;)
                {
                    final String[] split = line.split("=");
                    LOG.info("readLine: '{}'", split[0]);
                    if ("version".equals(split[0]))
                    {
                        final String version = Updater.isUpdateAvailable(split[1]);
                        if (version != null)
                        {
                            LOG.info("version found");
                            UpdaterWindow.main(version);
                            shouldShowMainWindow = true;
                        }
                    }
                }
            }
        }
        return shouldShowMainWindow;
    }

    public static boolean isDirty()
    {
        return isDirty;
    }

    public static void setDirty(final boolean argIsDirty)
    {
        Main.isDirty = argIsDirty;
    }
}
