package de.robadd.festivalmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Updater
{
	private Updater()
	{
	}

	public static void update()
	{
		//
	}

	public static String newerUpdate(final String oldVersion)
	{
		Version oldV = new Version(oldVersion);
		Version newV = listVersions().get(0);
		if (oldV.compareTo(newV) < 0)
		{
			return newV.toString();
		}
		return null;
	}

	public static List<Version> listVersions()
	{
		List<Version> retVal = new ArrayList<>();
		try
		{
			URL url = new URL("https://reiserdorfer-haisl.de/festivalmanager.php");
			InputStream inputStream = url.openConnection().getInputStream();
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			for (String line; (line = reader.readLine()) != null;)
			{
				String val = line.replace("FestivalManager-", "").replace(".jar", "");
				retVal.add(new Version(val));
			}
			retVal.sort((a, b) -> b.compareTo(a));
		}
		catch (IOException e)
		{
			//
		}
		return retVal;
	}

	static class Version implements Comparable<Version>
	{
		int major;
		int minor;
		int dev;
		boolean snapshot;

		@Override
		public int compareTo(final Version o)
		{
			if (snapshot || o.snapshot)
			{
				return 1;
			}
			if (this.equals(o))
			{
				return 0;
			}
			if (major < o.major)
			{
				return -1;
			}
			else if (major > o.major)
			{
				return 1;
			}

			if (minor < o.minor)
			{
				return -1;
			}
			else if (minor > o.minor)
			{
				return 1;
			}

			if (dev < o.dev)
			{
				return -1;
			}
			else if (dev > o.dev)
			{
				return 1;
			}
			return 0;
		}

		@Override
		public String toString()
		{
			return major + "." + minor + "." + dev + (snapshot ? "-SNAPSHOT" : "");
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Objects.hash(dev, major, minor, snapshot);
			return result;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (getClass() != obj.getClass())
			{
				return false;
			}
			Version other = (Version) obj;
			return dev == other.dev && major == other.major && minor == other.minor && snapshot == other.snapshot;
		}

		Version(final String argVersion)
		{
			super();

			Matcher matcher = Pattern.compile(
				"(?<major>\\d+)(?:\\.?(?<minor>\\d+))?(?:\\.?(?<dev>\\d+))?(?<snapshot>-SNAPSHOT)?").matcher(argVersion);
			if (matcher.matches())
			{
				if (matcher.group("major") != null)
				{
					this.major = Integer.parseInt(matcher.group("major"));
				}
				if (matcher.group("minor") != null)
				{
					this.minor = Integer.parseInt(matcher.group("minor"));
				}
				if (matcher.group("dev") != null)
				{
					this.dev = Integer.parseInt(matcher.group("dev"));
				}
				if (matcher.group("snapshot") != null)
				{
					this.snapshot = true;
				}
			}

		}
	}

}
