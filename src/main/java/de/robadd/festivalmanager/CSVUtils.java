package de.robadd.festivalmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtils
{
	private CSVUtils()
	{
	}

	public static <T extends CSVWritable> List<T> readFromCsv(final File csvFile, final Class<T> clazz)
	{
		List<T> retVal = new ArrayList<>();

		try
		{
			Files.readAllLines(csvFile.toPath()).stream()
					.map(e ->
					{
						try
						{
							T newInstance = clazz.newInstance();
							newInstance.fillfromCsv(e);
							return newInstance;
						}
						catch (InstantiationException | IllegalAccessException e1)
						{
							e1.printStackTrace();
							return null;
						}
					})
					.forEach(retVal::add);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return retVal;
	}

	public static <T extends CSVWritable> void writeToCsv(final List<T> tickets, final File file)
	{
		try
		{
			Files.write(file.toPath(), tickets.stream().map(CSVWritable::toCsv).collect(Collectors.toList()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
