package de.robadd.festivalmanager;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Band implements CSVWritable
{
	private LocalDateTime from;
	private LocalDateTime to;
	private String name;
	private boolean live;

	public Band()
	{
	}

	public Band(final String csv)
	{
		String[] values = csv.split(";");
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
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
			return objectMapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void fillfromCsv(final String line)
	{
		String[] values = line.split(";");
		name = values[0];
		from = LocalDateTime.parse(values[1]);
		to = LocalDateTime.parse(values[2]);
		live = Boolean.valueOf(values[3]);
	}

}
