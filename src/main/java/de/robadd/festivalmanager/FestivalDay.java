package de.robadd.festivalmanager;

import java.time.LocalDate;

public enum FestivalDay
{
    THURSDAY_2022("Donnerstag", 2022, 8, 4), FRIDAY_2022("Freitag", 2022, 8, 5), SATURDAY_2022("Samstag", 2022, 8, 6);

    private String name;
    private Integer year;
    private Integer day;
    private Integer month;

    FestivalDay(final String name, final Integer year, final Integer day, final Integer month)
    {
        this.name = name;
        this.year = year;
        this.day = day;
        this.month = month;
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
