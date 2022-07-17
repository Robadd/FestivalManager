package de.robadd.festivalmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.robadd.festivalmanager.ui.AttendeeEntry;

public class CSVUtils
{
	private CSVUtils()
	{
	}

	public static List<Ticket> readCsvToTickets(final File csvFile)
	{
		List<Ticket> retVal = new ArrayList<>();

		try
		{
			Files.readAllLines(csvFile.toPath()).stream()
					.map(Ticket::new)
					.forEach(retVal::add);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return retVal;
	}

	public static void writeTicketsToCsv(final List<Ticket> tickets, final File file)
	{
		try
		{
			Files.write(file.toPath(), tickets.stream().map(Ticket::toCsv).collect(Collectors.toList()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void writeTicketEntriesToCsv(final List<AttendeeEntry> tickets, final File file)
	{
		try
		{
			Files.write(file.toPath(), tickets.stream().map(AttendeeEntry::toCsvLine).collect(Collectors.toList()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
