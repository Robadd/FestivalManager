package de.robadd.festivalmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CSVUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(CSVUtils.class);

    private CSVUtils()
    {
    }

    public static <T extends CSVWritable> List<T> readFromCsv(final File csvFile, final Class<T> clazz)
    {
        final List<T> retVal = new ArrayList<>();

        try
        {
            Files.readAllLines(csvFile.toPath()).stream()
                    .map(e ->
                    {
                        try
                        {
                            final T newInstance = clazz.newInstance();
                            newInstance.fillfromCsv(e);
                            return newInstance;
                        }
                        catch (InstantiationException | IllegalAccessException e1)
                        {
                            LOG.error("Illegal Access", e1);
                            return null;
                        }
                    })
                    .forEach(retVal::add);
        }
        catch (final IOException e)
        {
            LOG.error("CSV file not found", e);
        }
        return retVal;
    }

    public static <T extends CSVWritable> void writeToCsv(final List<T> tickets, final File file)
    {
        try
        {
            Files.write(file.toPath(), tickets.stream().map(CSVWritable::toCsv).collect(Collectors.toList()));
        }
        catch (final IOException e)
        {
            LOG.error("CSV file not found");
        }
    }

}
