package de.robadd.festivalmanager.updater;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version>
{
	int major;
	int minor;
	int dev;
	boolean snapshot;

	/**
	 * @return the major
	 */
	int getMajor()
	{
		return major;
	}

	/**
	 * @return the minor
	 */
	int getMinor()
	{
		return minor;
	}

	/**
	 * @return the dev
	 */
	int getDev()
	{
		return dev;
	}

	/**
	 * @return the snapshot
	 */
	boolean isSnapshot()
	{
		return snapshot;
	}

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

	public Version(final String argVersion)
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
