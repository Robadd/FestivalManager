package de.robadd.festivalmanager.updater;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Update
{
    private static final Logger LOG = LoggerFactory.getLogger(Update.class);
    private Version version;
    private String releaseNotes;
    private List<String> dependencies;

    public Update()
    {
    }

    public Update(final Version version, final String releaseNotes, final List<String> dependencies)
    {
        super();
        this.version = version;
        this.releaseNotes = releaseNotes;
        this.dependencies = dependencies;
    }

    public static Update from(final String json)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Update.class);

        }
        catch (JsonProcessingException e)
        {
            LOG.error("Could not process JSON", e);
        }
        return null;
    }

    public static List<Update> fromList(final String json)
    {
        try
        {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<Update>>()
            {
            });

        }
        catch (JsonProcessingException e)
        {
            LOG.error("Could not process JSON", e);
        }
        return Collections.emptyList();
    }

    /**
     * @return the version
     */
    public Version getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final Version version)
    {
        this.version = version;
    }

    /**
     * @param version the version to set
     */
    @JsonSetter("version")
    public void setVersion(final String version)
    {
        this.version = new Version(version);
    }

    /**
     * @return the releaseNotes
     */
    public String getReleaseNotes()
    {
        return releaseNotes;
    }

    /**
     * @param releaseNotes the releaseNotes to set
     */
    public void setReleaseNotes(final String releaseNotes)
    {
        this.releaseNotes = releaseNotes;
    }

    /**
     * @return the dependencies
     */
    public List<String> getDependencies()
    {
        return dependencies;
    }

    /**
     * @param dependencies the dependencies to set
     */
    public void setDependencies(final List<String> dependencies)
    {
        this.dependencies = dependencies;
    }

}
