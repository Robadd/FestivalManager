package de.robadd.festivalmanager.model;

import java.sql.Date;
import java.util.Objects;

public class ConfigEntry<T>
{
    private T value;
    private String description;
    private String name;
    private Integer year;

    public static ConfigEntry<?> of(final String argName, final String desc,
            final Integer argYear, final String type,
            final String strv, final Date datev, final Integer intv, final Boolean boolv)
    {
        switch (type)
        {
        case "string_value":
            return new ConfigEntry<>(strv, desc, argName, argYear);
        case "date_value":
            return new ConfigEntry<>(datev, desc, argName, argYear);
        case "int_value":
            return new ConfigEntry<>(intv, desc, argName, argYear);
        case "bool_value":
            return new ConfigEntry<>(boolv, desc, argName, argYear);
        default:
            return null;
        }
    }

    private ConfigEntry(final T value, final String description, final String name, final Integer year)
    {
        super();
        this.value = value;
        this.description = description;
        this.name = name;
        this.year = year;
    }

    /**
     * @return the value
     */
    public T getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final T value)
    {
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the year
     */
    public Integer getYear()
    {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(final Integer year)
    {
        this.year = year;
    }

    @Override
    public String toString()
    {
        return "ConfigEntry [value=" + value + ", description=" + description + ", name=" + name + ", year=" + year
                + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(description, name, value, year);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        ConfigEntry other = (ConfigEntry) obj;
        return Objects.equals(description, other.description) && Objects.equals(name, other.name) && Objects.equals(
            value, other.value) && Objects.equals(year, other.year);
    }

}
