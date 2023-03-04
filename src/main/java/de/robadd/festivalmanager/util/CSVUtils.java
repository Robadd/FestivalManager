package de.robadd.festivalmanager.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.model.type.CSVWritable;

public final class CSVUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(CSVUtils.class);

    private CSVUtils()
    {
    }

    /**
     * @param <T>     the class
     * @param csvFile the csv file
     * @param clazz   the clazz
     * @return list of csvwritables
     */
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

    /**
     *
     * @param <T>     the class
     * @param tickets csvwritables to write
     * @param file    the file to write to
     * @deprecated since 0.3.1
     */
    @Deprecated
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
