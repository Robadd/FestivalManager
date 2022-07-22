package de.robadd.festivalmanager;

public interface CSVWritable
{

    void fillfromCsv(String line);

    String toCsv();
}
