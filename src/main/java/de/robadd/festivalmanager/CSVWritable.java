package de.robadd.festivalmanager;

public interface CSVWritable
{

	public void fillfromCsv(final String line);

	public String toCsv();
}
