package de.robadd.festivalmanager.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.model.annotation.LoadFinalizing;
import de.robadd.festivalmanager.model.type.Identifiable;

public class CrudRepository<T extends Identifiable>
{
    private static final String WHERE = " WHERE ";
    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";
    protected static final Logger LOG = LoggerFactory.getLogger(CrudRepository.class);
    private final TableMetaData<T> metaData;

    public static <S extends Identifiable> CrudRepository<S> of(final Class<S> argClazz)
    {
        return new CrudRepository<>(argClazz);
    }

    protected CrudRepository(final Class<T> argClazz)
    {
        super();
        metaData = TableMetaData.from(argClazz);
    }

    public T getById(final int id)
    {
        T retVal = null;
        final String tableName = metaData.getTableName();
        final String sql = SELECT
                + metaData.getFieldsMeta().stream()
                        .map(DbMetaData::getColumnName)
                        .map(a -> tableName + '.' + a)
                        .collect(Collectors.joining(","))
                + FROM
                + tableName
                + WHERE
                + metaData.getIdField() + " = " + id;
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {
            if (rs.next())
            {
                retVal = getObject(rs, metaData);
            }
        }
        catch (final Exception e)
        {
            LOG.error("Error while loading", e);
        }

        return retVal;
    }

    public boolean delete(final T value)
    {
        final String tableName = metaData.getTableName();
        final String sql = "DELETE "
                + FROM
                + tableName
                + WHERE + metaData.getIdField() + " = " + value.getId();
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();)
        {
            int rs = stmt.executeUpdate(sql);
            if (rs > 0)
            {
                return true;
            }
        }
        catch (final Exception e)
        {
            LOG.error("Error while loading", e);
        }

        return false;
    }

    public boolean create(final T val, final Connection conn)
    {
        final StringBuilder sql = new StringBuilder("INSERT INTO ")
                .append(metaData.getTableName())
                .append(" (")
                .append(metaData.getFieldsMeta().stream().map(DbMetaData::getColumnName).collect(Collectors.joining(
                    ",")))
                .append(") VALUES (");
        sql.append(
            metaData.getFieldsMeta().stream()
                    .map(DbMetaData::getGetter)
                    .map(a -> getValue(val, a))
                    .collect(Collectors.joining(",")));
        sql.append(")");

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql.toString()))
        {
            return true;
        }
        catch (final SQLException e)
        {
            return false;
        }
    }

    public boolean create(final T val)
    {
        try (Connection conn = getConnection();)
        {
            return create(val, conn);
        }
        catch (final SQLException e)
        {
            return false;
        }
    }

    public List<T> getByFilter(final String colName, final Object value)
    {
        final List<T> retVal = new ArrayList<>();

        final String tableName = metaData.getTableName();
        final String sql = SELECT
                + metaData.getFieldsMeta().stream()
                        .map(DbMetaData::getColumnName)
                        .map(a -> tableName + '.' + a)
                        .collect(Collectors.joining(","))
                + FROM
                + tableName
                + WHERE + colName + " = " + value.toString();
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                retVal.add(getObject(rs, metaData));
            }
        }
        catch (final Exception e)
        {
            LOG.error("", e);
        }

        return retVal;
    }

    public List<T> getAll()
    {
        final List<T> retVal = new ArrayList<>();

        final String tableName = metaData.getTableName();
        final String sql = SELECT
                + metaData.getFieldsMeta().stream()
                        .map(DbMetaData::getColumnName)
                        .map(a -> tableName + '.' + a)
                        .collect(Collectors.joining(","))
                + FROM
                + tableName;
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                retVal.add(getObject(rs, metaData));
            }
        }
        catch (final Exception e)
        {
            LOG.error("", e);
        }

        return retVal;
    }

    protected Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:mariadb://s193.goserver.host/web127_db1", "web127",
            "8VDZuNiZKTBQCkZdlDE3");
    }

    private String getValue(final T val, final Method a)
    {
        try
        {
            Object invoke = a.invoke(val);
            if (invoke == null)
            {
                return null;
            }
            if (invoke instanceof Boolean)
            {
                return Boolean.TRUE.equals(invoke) ? "1" : "0";
            }
            else if (invoke instanceof Integer)
            {
                return invoke.toString();
            }
            return "\"" + invoke + "\"";
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
        {
            return null;
        }
    }

    private static <T> T getObject(final ResultSet rs, final TableMetaData<T> metaData)
    {
        try
        {
            final T obj = metaData.getClazz().newInstance();
            for (final DbMetaData dbMetaData : metaData.getFieldsMeta())
            {
                setData(obj, dbMetaData, rs);
                if (obj instanceof LoadFinalizing)
                {
                    ((LoadFinalizing) obj).afterLoading();
                }
            }
            return obj;
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            LOG.error("", e);
        }
        return null;
    }

    private static void setData(final Object obj, final DbMetaData fieldMeta, final ResultSet rs)
    {
        try
        {
            final String columnName = fieldMeta.getColumnName();
            final Class<?> clazz = fieldMeta.getClazz();
            final Method setter = fieldMeta.getSetter();

            if (clazz.isAssignableFrom(String.class))
            {
                final String val = rs.getString(columnName);
                setter.invoke(obj, val);
            }
            else if (clazz.isAssignableFrom(Integer.class))
            {
                final Integer val = rs.getInt(columnName);
                setter.invoke(obj, val);
            }
            else if (clazz.isAssignableFrom(Date.class))
            {
                final java.sql.Date val = rs.getDate(columnName);
                setter.invoke(obj, val);
            }
            else if (clazz.isAssignableFrom(LocalDateTime.class))
            {
                final java.sql.Timestamp val = rs.getTimestamp(columnName);
                setter.invoke(obj, val.toLocalDateTime());
            }
            else if (clazz.isAssignableFrom(Float.class))
            {
                final Float val = rs.getFloat(columnName);
                setter.invoke(obj, val);
            }
            else if (clazz.isAssignableFrom(Boolean.class))
            {
                final Boolean val = rs.getBoolean(columnName);
                setter.invoke(obj, val);
            }
            else
            {
                final Object val = rs.getObject(columnName);
                setValue(obj, setter, val, rs.wasNull());
            }
        }
        catch (ReflectiveOperationException | IllegalArgumentException | SQLException e)
        {
            LOG.error("", e);
        }
    }

    private static void setValue(final Object obj, final Method setter, final Object val, final boolean wasNull)
            throws IllegalAccessException, InvocationTargetException
    {
        if (!wasNull)
        {
            setter.invoke(obj, val);
        }
    }

    /**
     * Update
     *
     * @param value value to update
     * @param conn  Connection
     * @return if row was updated
     */
    public boolean update(final T value, final Connection conn)
    {
        final StringBuilder sql = new StringBuilder("UPDATE ")
                .append(metaData.getTableName())
                .append(" SET ")
                .append(metaData.getFieldsMeta().stream().filter(a -> !a.isId())
                        .map(a -> a.getColumnName() + " = " + getValue(value, a.getGetter()))
                        .collect(Collectors.joining(",")))
                .append(WHERE)
                .append(metaData.getIdField())
                .append(" = ")
                .append(value.getId());

        try (Statement stmt = conn.createStatement();)
        {
            return 1 == stmt.executeUpdate(sql.toString());
        }
        catch (final SQLException e)
        {
            return false;
        }
    }

    public boolean save(final List<T> tickets)
    {
        try (Connection con = getConnection())
        {
            int rowCount = 0;
            con.setAutoCommit(false);
            List<T> dbTickets = getAll();

            for (T t : tickets)
            {
                if (update(t, con) || create(t, con))
                {
                    dbTickets.removeIf(a -> t.getId().equals(a.getId()));
                    rowCount++;
                }
            }
            dbTickets.forEach(CrudRepository.this::delete);

            if (rowCount == tickets.size())
            {
                con.commit();
                return true;
            }
            else
            {
                con.rollback();
                return false;
            }
        }
        catch (SQLException e)
        {
            LOG.error("", e);
            return false;
        }
    }
}
