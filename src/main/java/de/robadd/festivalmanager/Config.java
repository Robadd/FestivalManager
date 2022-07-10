package de.robadd.festivalmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class Config
{
	private static final String UTF_8 = "UTF-8";
	private static final Config INSTANCE = new Config();
	Map<String, String> map = new HashMap<>();
	private static final String SAVE_PATH = "savePath";
	private static final String CSV_FILE = "csvFile";
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
				setCsvFile(new File(new File(URLDecoder.decode(path, UTF_8)).getParentFile().getPath(), "2022.csv")
						.getPath());
				setSavePath(new File(URLDecoder.decode(path, UTF_8)).getParentFile().getPath() + "\\");
				saveIni();
				loadIni(file);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public static synchronized Config getInstance()
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
		try (FileWriter fileWriter = new FileWriter(file, false))
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

	public void setCsvFile(final String path)
	{
		setParam(CSV_FILE, path);
	}

	public String getSavePath()
	{
		return getValue(SAVE_PATH);
	}

	public String getCsvFile()
	{
		return getValue(CSV_FILE);
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
