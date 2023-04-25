package de.robadd.festivalmanager.model;

public enum TicketType
{
    ALL_DAY(2, 2023, "All-Day Festival-Ticket", 40.00),
    ONE_DAY(1, 2023, "One-Day Festival-Ticket", 20.00),
    VISITOR(3, 2023, "Zuschauer Ticket", 10.00);

    private String name;
    private Integer id;
    private Double price;
    private Integer year;

    private TicketType(final Integer id, final Integer year, final String name, final Double price)
    {
        this.name = name;
        this.id = id;
        this.price = price;
        this.year = year;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @return the price
     */
    public Double getPrice()
    {
        return price;
    }

    /**
     * @return the year
     */
    public Integer getYear()
    {
        return year;
    }

    public static TicketType forIdAndYear(final Integer id, final Integer year)
    {
        for (TicketType ticketType : values())
        {
            if (ticketType.id.equals(id) && ticketType.year.equals(year))
            {
                return ticketType;
            }
        }
        return null;
    }
}
