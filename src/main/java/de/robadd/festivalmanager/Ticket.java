package de.robadd.festivalmanager;

import java.text.MessageFormat;
import java.util.Objects;

public class Ticket implements CSVWritable
{
    private String name;
    private Integer type;
    private Boolean tShirt;
    private String hash;
    private Boolean paid = false;
    private Boolean sent = false;

    public Ticket()
    {
        super();
    }

    public Ticket(final String csv)
    {
        final String[] values = csv.split(";");
        name = values[0];
        type = Integer.valueOf(values[1]);
        tShirt = Boolean.parseBoolean(values[2]);
        paid = Boolean.parseBoolean(values[3]);
        sent = Boolean.parseBoolean(values[4]);
        hash = Crypto.generateHash(name, type, tShirt);
    }

    public Ticket(final String name, final Integer type, final Boolean tShirt)
    {
        super();
        this.name = name;
        this.type = type;
        this.tShirt = tShirt;
        this.hash = Crypto.generateHash(name, type, tShirt);
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @param type the type to set
     */
    public void setType(final Integer type)
    {
        this.type = type;
    }

    /**
     * @param argTshirt the tShirt to set
     */
    public void setTShirt(final Boolean argTshirt)
    {
        this.tShirt = argTshirt;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(final String hash)
    {
        this.hash = hash;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(final Boolean paid)
    {
        this.paid = paid;
    }

    /**
     * @param sent the sent to set
     */
    public void setSent(final Boolean sent)
    {
        this.sent = sent;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * @return the tShirt
     */
    public Boolean getTShirt()
    {
        return tShirt;
    }

    /**
     * @return the paid
     */
    public boolean isPaid()
    {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(final boolean paid)
    {
        this.paid = paid;
    }

    /**
     * @return the sent
     */
    public boolean isSent()
    {
        return sent;
    }

    /**
     * @param sent the sent to set
     */
    public void setSent(final boolean sent)
    {
        this.sent = sent;
    }

    @Override
    public String toCsv()
    {
        return MessageFormat.format("{0};{1};{2};{3};{4}", name, type, tShirt, paid, sent);
    }

    @Override
    public String toString()
    {
        return "{"
                + "\"name\":\"" + name + "\","
                + "\"type\":\"" + type + "\","
                + "\"tshirt\":\"" + tShirt + "\","
                + "\"hash\":\"" + hash + "\""
                + "}";
    }

    public String getHash()
    {
        return hash;
    }

    public Ticket(final String name, final Integer type, final Boolean tShirt, final Boolean paid, final Boolean sent)
    {
        super();
        this.name = name;
        this.type = type;
        this.tShirt = tShirt;
        this.paid = paid;
        this.sent = sent;
        hash = Crypto.generateHash(name, type, tShirt);
    }

    @Override
    public void fillfromCsv(final String line)
    {
        final String[] values = line.split(";");
        name = values[0];
        type = Integer.valueOf(values[1]);
        tShirt = Boolean.parseBoolean(values[2]);
        paid = Boolean.parseBoolean(values[3]);
        sent = Boolean.parseBoolean(values[4]);
        hash = Crypto.generateHash(name, type, tShirt);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(hash, name, paid, sent, tShirt, type);
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
        Ticket other = (Ticket) obj;
        return Objects.equals(hash, other.hash) && Objects.equals(name, other.name) && Objects.equals(paid, other.paid)
                && Objects.equals(sent, other.sent) && Objects.equals(tShirt, other.tShirt) && Objects.equals(type,
                    other.type);
    }

}
