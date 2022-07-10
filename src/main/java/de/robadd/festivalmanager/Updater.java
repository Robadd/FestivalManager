package de.robadd.festivalmanager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Updater
{
	private Updater()
	{
	}

	public static void update()
	{
		//
	}

	public static boolean newerUpdate(final String oldVersion, final String newVersion)
	{
		Version oldV = new Version(oldVersion);
		Version newV = new Version(newVersion);
		return oldV.compareTo(newV) < 0;
	}

	private static class Version implements Comparable<Version>
	{
		int major;
		int minor;
		int dev;

		@Override
		public int compareTo(final Version o)
		{
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
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Objects.hash(dev, major, minor);
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
			return dev == other.dev && major == other.major && minor == other.minor;
		}

		Version(final String version)
		{
			super();
			List<String> list = Arrays.asList(version.split("\\."));
			try
			{
				this.major = Integer.parseInt(list.get(0));
				this.minor = Integer.parseInt(list.get(1));
				this.dev = Integer.parseInt(list.get(2));
			}
			catch (IndexOutOfBoundsException | NumberFormatException e)
			{
				// Workflow
			}
		}
	}

}
