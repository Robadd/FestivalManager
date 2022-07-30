package de.robadd.festivalmanager.updater;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.robadd.festivalmanager.Band;
import de.robadd.festivalmanager.CSVWritable;
import de.robadd.festivalmanager.Ticket;
import de.robadd.festivalmanager.ui.AttendeeEntry;

public class CSVWritableTest
{
    @Test(dataProvider = "data")
    public void serializingTest(final CSVWritable input, final Class<? extends CSVWritable> clazz,
            final String expected)
    {
        Assertions.assertThat(input.toCsv()).isEqualTo(expected);
    }

    @Test(dataProvider = "data")
    public void deSerializingTest(final CSVWritable expected, final Class<? extends CSVWritable> clazz,
            final String string) throws Exception
    {
        CSVWritable actual = clazz.newInstance();
        actual.fillfromCsv(string);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DataProvider(name = "data")
    public Object[][] data()
    {
        return new Object[][] {
                {new Band(LocalDateTime.of(2022, 8, 6, 20, 30), LocalDateTime.of(2022, 8, 6, 21, 00), "Foobar", false),
                        Band.class, "Foobar;2022-08-06T20:30;2022-08-06T21:00;false"},
                {new Ticket("Michael Mustermann", 2, false, true, false), Ticket.class,
                        "Michael Mustermann;2;false;true;false"},
                {new AttendeeEntry(new Ticket("Michael Mustermann", 2, false, true, false), 0), AttendeeEntry.class,
                        "Michael Mustermann;2;false;true;false"}

        };
    }
}
