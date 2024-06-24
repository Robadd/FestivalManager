package de.robadd.festivalmanager.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public enum FestivalDay implements Comparable<FestivalDay>
{
    THURSDAY_2022(FestivalDay.DONNERSTAG, 2022, 8, 4),
    FRIDAY_2022(FestivalDay.FREITAG, 2022, 8, 5),
    SATURDAY_2022(FestivalDay.SAMSTAG, 2022, 8, 6),
    THURSDAY_2023(FestivalDay.DONNERSTAG, 2023, 7, 27),
    FRIDAY_2023(FestivalDay.FREITAG, 2023, 7, 28),
    SATURDAY_2023(FestivalDay.SAMSTAG, 2023, 7, 29),
    THURSDAY_2024(FestivalDay.DONNERSTAG, 2024, 7, 25),
    FRIDAY_2024(FestivalDay.FREITAG, 2024, 7, 26),
    SATURDAY_2024(FestivalDay.SAMSTAG, 2024, 7, 27);

    private static final String SAMSTAG = "Samstag";
    private static final String FREITAG = "Freitag";
    private static final String DONNERSTAG = "Donnerstag";
    private String name;
    private Integer year;
    private Integer day;
    private Integer month;

    FestivalDay(final String name, final Integer year, final Integer month, final Integer day)
    {
        this.name = name;
        this.year = year;
        this.day = day;
        this.month = month;
    }

    public static FestivalDay[] forYear(final int year)
    {
        List<FestivalDay> retVal = new ArrayList<>();
        for (FestivalDay festivalDay : values())
        {
            if (festivalDay.getYear() == year)
            {
                retVal.add(festivalDay);
            }
        }
        retVal.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return retVal.toArray(new FestivalDay[0]);
    }

    public static FestivalDay from(final LocalDateTime from)
    {
        int year = from.get(ChronoField.YEAR);
        int month = from.get(ChronoField.MONTH_OF_YEAR);
        int day = from.get(ChronoField.DAY_OF_MONTH);

        for (FestivalDay festivalDay : values())
        {
            if (festivalDay.getDay() == day && festivalDay.getMonth() == month && festivalDay.getYear() == year)
            {
                return festivalDay;
            }
        }
        return null;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the year
     */
    public Integer getYear()
    {
        return year;
    }

    /**
     * @return the day
     */
    public Integer getDay()
    {
        return day;
    }

    /**
     * @return the month
     */
    public Integer getMonth()
    {
        return month;
    }

    public LocalDate getDate()
    {
        return LocalDate.of(year, month, day);
    }
}
