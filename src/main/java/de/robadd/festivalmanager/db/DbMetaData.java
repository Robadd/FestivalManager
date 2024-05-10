package de.robadd.festivalmanager.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DbMetaData
{
    private boolean isId;
    private Method getter;
    private Method setter;
    private String columnName;
    private Field field;
    private Class<?> clazz;

    /**
     * @return the columnName
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(final String columnName)
    {
        this.columnName = columnName;
    }

    /**
     * @return the getter
     */
    public Method getGetter()
    {
        return getter;
    }

    /**
     * @param getter the getter to set
     */
    public void setGetter(final Method getter)
    {
        this.getter = getter;
    }

    /**
     * @return the setter
     */
    public Method getSetter()
    {
        return setter;
    }

    /**
     * @param setter the setter to set
     */
    public void setSetter(final Method setter)
    {
        this.setter = setter;
    }

    /**
     * @return the field
     */
    public Field getField()
    {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(final Field field)
    {
        this.field = field;
    }

    /**
     * @return the clazz
     */
    public Class<?> getClazz()
    {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(final Class<?> clazz)
    {
        this.clazz = clazz;
    }

    public boolean isId()
    {
        return isId;
    }

    public void setId(final boolean isId)
    {
        this.isId = isId;
    }

}
