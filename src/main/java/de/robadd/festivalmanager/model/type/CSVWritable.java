package de.robadd.festivalmanager.model.type;

public interface CSVWritable
{

    void fillfromCsv(String line);

    String toCsv();
}
