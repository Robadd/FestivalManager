package de.robadd.festivalmanager.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.util.StringUtils;

import de.robadd.festivalmanager.model.annotation.DbField;
import de.robadd.festivalmanager.model.annotation.DbTable;
import de.robadd.festivalmanager.model.annotation.Id;

public class TableMetaData<T>
{
    private String tableName;
    private String idField;
    private List<DbMetaData> fieldsMeta;
    private Class<T> clazz;

    public static <T> TableMetaData<T> from(final Class<T> clazz)
    {
        TableMetaData<T> metaData = new TableMetaData<>();
        metaData.clazz = clazz;
        metaData.fieldsMeta = new ArrayList<>();

        final DbTable tableAnnotation = clazz.getAnnotation(DbTable.class);
        if (tableAnnotation == null)
        {
            return null;
        }
        metaData.tableName = tableAnnotation.value();

        for (final Field field : clazz.getDeclaredFields())
        {
            final DbField fieldAnnotation = field.getAnnotation(DbField.class);
            if (fieldAnnotation != null)
            {
                final Id idAnnotation = field.getAnnotation(Id.class);
                try
                {
                    final DbMetaData meta = new DbMetaData();
                    final String columnName = fieldAnnotation.value();
                    final String getterName = "get" + StringUtils.capitalize(field.getName());
                    final String setterName = "set" + StringUtils.capitalize(field.getName());
                    final Class<?> type = field.getType();
                    meta.setClazz(type);
                    meta.setField(field);
                    meta.setGetter(clazz.getMethod(getterName));
                    meta.setSetter(clazz.getMethod(setterName, type));
                    meta.setColumnName(columnName);
                    if (idAnnotation != null)
                    {
                        metaData.idField = columnName;
                    }
                    metaData.getFieldsMeta().add(meta);
                }
                catch (NoSuchMethodException | SecurityException e)
                {
                }
            }

        }
        return metaData;
    }

    /**
     * @return the idField
     */
    public String getIdField()
    {
        return idField;
    }

    /**
     * @return the tableName
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @return the fieldsMeta
     */
    public List<DbMetaData> getFieldsMeta()
    {
        return fieldsMeta;
    }

    /**
     * @return the clazz
     */
    public Class<T> getClazz()
    {
        return clazz;
    }
}
