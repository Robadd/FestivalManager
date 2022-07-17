package de.robadd.festivalmanager;

import java.time.LocalDateTime;

public class Band
{
	private LocalDateTime from;
	private LocalDateTime to;
	private String name;
	private boolean live;

	public Band()
	{
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

}
