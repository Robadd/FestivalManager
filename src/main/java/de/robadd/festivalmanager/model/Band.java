package de.robadd.festivalmanager.model;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.robadd.festivalmanager.model.annotation.DbField;
import de.robadd.festivalmanager.model.annotation.DbTable;
import de.robadd.festivalmanager.model.annotation.Id;
import de.robadd.festivalmanager.model.type.CSVWritable;
import de.robadd.festivalmanager.model.type.Identifiable;
import de.robadd.festivalmanager.model.type.JSONWritable;

/**
 * Band
 *
 * @author Robert Kraus
 */
@DbTable("band")
public class Band implements CSVWritable, JSONWritable, Identifiable
{
    private static final Logger LOG = LoggerFactory.getLogger(Band.class);
    @Id
    @DbField("id")
    private int id;
    @DbField("from")
    private LocalDateTime from;
    @DbField("to")
    private LocalDateTime to;
    @DbField("name")
    private String name;
    @DbField("live")
    private Boolean live;

    public Band()
    {
        super();
    }

    public Band(final LocalDateTime from, final LocalDateTime to, final String name, final boolean live)
    {
        super();
        this.from = from;
        this.to = to;
        this.name = name;
        this.live = live;
    }

    /**
     * @return the from
     */
    public LocalDateTime getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(final LocalDateTime from)
    {
        this.from = from;
    }

    /**
     * @return the to
     */
    public LocalDateTime getTo()
    {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final LocalDateTime to)
    {
        this.to = to;
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
     * @return the live
     */
    public Boolean getLive()
    {
        return live;
    }

    /**
     * @param live the live to set
     */
    public void setLive(final Boolean live)
    {
        this.live = live;
    }

    @Override
    public String toCsv()
    {
        return MessageFormat.format("{0};{1};{2};{3}", name, from, to, live);
    }

    @Override
    public void fillFromJson(final String json)
    {
        try
        {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
            final Band val = objectMapper.readValue(json, Band.class);
            this.from = val.from;
            this.to = val.to;
            this.name = val.name;
            this.live = val.live;
        }
        catch (JsonProcessingException e)
        {
            LOG.error("Could not serialize from JSON", e);
        }
    }

    @Override
    public String toJson()
    {
        String retVal = "";
        try
        {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
            retVal = objectMapper.writeValueAsString(this);
        }
        catch (final JsonProcessingException e)
        {
            LOG.error("Error while writing as JSON string", e);
        }
        return retVal;
    }

    @Override
    public void fillfromCsv(final String line)
    {
        final String[] values = line.split(";");
        name = values[0];
        from = LocalDateTime.parse(values[1]);
        to = LocalDateTime.parse(values[2]);
        live = Boolean.valueOf(values[3]);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(from, live, name, to);
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
        Band other = (Band) obj;
        return Objects.equals(from, other.from) && live == other.live && Objects.equals(name, other.name) && Objects
                .equals(to, other.to);
    }

    @Override
    public Integer getId()
    {
        return id;
    }

    @Override
    public void setId(final Integer argId)
    {
        id = argId;
    }

    @Override
    public String toString()
    {
        return toJson();
    }
}
