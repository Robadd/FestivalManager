package de.robadd.festivalmanager.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import de.robadd.festivalmanager.model.Ticket;
import de.robadd.festivalmanager.util.Config;

public class TicketRepository extends CrudRepository<Ticket>
{
    public TicketRepository()
    {
        super(Ticket.class);
    }

    @Override
    public boolean save(final List<Ticket> tickets)
    {

        try (Connection con = getConnection())
        {
            int rowCount = 0;
            con.setAutoCommit(false);
            List<Ticket> dbTickets = getAll();

            for (Ticket t : tickets)
            {
                if (update(t, con) || create(t, con))
                {
                    dbTickets.removeIf(a -> t.getId().equals(a.getId()));
                    rowCount++;
                }
            }
            dbTickets.stream()
                    .filter(a -> Config.YEAR.equals(a.getYear()))
                    .forEach(TicketRepository.this::delete);

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
