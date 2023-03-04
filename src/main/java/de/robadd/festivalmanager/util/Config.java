package de.robadd.festivalmanager.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.Main;

public final class Config
{
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);
    private static final String UTF_8 = "UTF-8";
    private static final Config INSTANCE = new Config();
    private final Map<String, String> map = new ConcurrentHashMap<>();
    private static final String SAVE_PATH = "savePath";
    private static final String ATTENDEES_CSV_FILE = "csvFile";
    private static final String BANDS_CSV_FILE = "bandsCsvFile";
    private static final String CHROME_PATH = "chromePath";
    private static final String SECRET_KEY = "secretKey";

    private Config()
    {
        try
        {
            final File file = getConfigFile();
            if (file.canRead())
            {
                loadIni(file);
            }
            else if (file.createNewFile())
            {
                final String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                setAttendeesCsvFile(new File(new File(URLDecoder.decode(path, UTF_8)).getParentFile().getPath(),
                        "2023.csv")
                                .getPath());
                setSavePath(new File(URLDecoder.decode(path, UTF_8)).getParentFile().getPath() + "\\");
                saveIni();
                loadIni(file);
            }
        }
        catch (final IOException e)
        {
            LOG.error("Error loading config file", e);
        }
    }

    public static Config getInstance()
    {
        return INSTANCE;
    }

    private File getConfigFile() throws UnsupportedEncodingException
    {
        final String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return new File(new File(URLDecoder.decode(path, UTF_8)).getParentFile().getPath(), "config.ini");
    }

    public void saveIni() throws IOException
    {
        final File file = getConfigFile();
        try (FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(file, Charset.defaultCharset(), false))
        {
            final String configContent = map.entrySet()
                    .stream()
                    .filter(a -> a.getKey() != null && a.getValue() != null)
                    .map(entry -> entry.getKey() + "\t" + entry.getValue())
                    .collect(Collectors.joining(System.lineSeparator()));
            fileWriter.write(configContent);
            fileWriter.flush();
        }
    }

    private void loadIni(final File config) throws IOException
    {
        for (final String string : FileUtils.readLines(config, StandardCharsets.UTF_8))
        {
            final String[] split = string.split("\t");
            final String key = split[0];
            final String value = split[1];
            map.put(key, value);
        }
    }

    public void setSavePath(final String path)
    {
        setParam(SAVE_PATH, path);
    }

    public void setAttendeesCsvFile(final String path)
    {
        setParam(ATTENDEES_CSV_FILE, path);
    }

    public String getSavePath()
    {
        return getValue(SAVE_PATH);
    }

    public String getAttendeesCsvFile()
    {
        return getValue(ATTENDEES_CSV_FILE);
    }

    public void setBandsCsvFile(final String path)
    {
        setParam(BANDS_CSV_FILE, path);
    }

    public String getBandsCsvFile()
    {
        return getValue(BANDS_CSV_FILE);
    }

    public void setParam(final String key, final String value)
    {
        map.put(key, value);
    }

    public Map<String, String> getMap()
    {
        return map;
    }

    private String getValue(final String key)
    {
        return map.get(key);
    }

    public String getChromePath()
    {
        return getValue(CHROME_PATH);
    }

    public String getSecretKey()
    {
        return getValue(SECRET_KEY);
    }
}
