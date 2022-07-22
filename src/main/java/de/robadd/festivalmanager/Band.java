package de.robadd.festivalmanager;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Band
 *
 * @author Robert Kraus
 */
public class Band implements CSVWritable
{
    private static final Logger LOG = LoggerFactory.getLogger(Band.class);
    private LocalDateTime from;
    private LocalDateTime to;
    private String name;
    private boolean live;

    public Band(final String csv)
    {
        final String[] values = csv.split(";");
        name = values[0];
        from = LocalDateTime.parse(values[1]);
        to = LocalDateTime.parse(values[2]);
        live = Boolean.valueOf(values[3]);
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
    public boolean isLive()
    {
        return live;
    }

    /**
     * @param live the live to set
     */
    public void setLive(final boolean live)
    {
        this.live = live;
    }

    @Override
    public String toCsv()
    {
        return MessageFormat.format("{0};{1};{2};{3}", name, from, to, live);
    }

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

}
