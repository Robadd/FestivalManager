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

    private Main()
    {
    }

//    public $host = "localhost";
//    public $dbName = "web127_db1";
//    public $username = "web127";
//    public $password = "8VDZuNiZKTBQCkZdlDE3";

    public static void main(final String[] args) throws Exception
    {

        if (showUpdateWindowIfNecessary())
        {
            MainWindow.main();
        }

    }

    private static boolean showUpdateWindowIfNecessary() throws IOException, ClassNotFoundException,
            InstantiationException,
            IllegalAccessException, UnsupportedLookAndFeelException
    {
        boolean shouldShowMainWindow = true;
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
                            shouldShowMainWindow = false;
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

    public static void setDirty(boolean isDirty)
    {
        Main.isDirty = isDirty;
    }
}
