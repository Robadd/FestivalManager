package de.robadd.festivalmanager.ui.tab;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.db.TicketRepository;
import de.robadd.festivalmanager.model.Ticket;
import de.robadd.festivalmanager.ui.button.AddTicketButton;
import de.robadd.festivalmanager.ui.entry.AttendeeEntry;
import de.robadd.festivalmanager.ui.util.PrintWorker;
import de.robadd.festivalmanager.util.CSVUtils;
import de.robadd.festivalmanager.util.Config;

public final class AttendeesTab extends JPanel
{

    private static final long serialVersionUID = 2113186135796490710L;
    private static final Logger LOG = LoggerFactory.getLogger(AttendeesTab.class);
    private final Panel entryList = new Panel();
    private final ScrollPane scrollPane = new ScrollPane();
    private final JPanel bottom = new JPanel();

    public AttendeesTab()
    {
        setMainLayout();
        getEntryList().setLayout(new BoxLayout(getEntryList(), BoxLayout.Y_AXIS));
        scrollPane.add(getEntryList(), entryListLayout());
        add(scrollPane, scrollPaneLayout());
        add(bottom, bottomLayout());
        initBottomPanel();
        loadFromDb();
    }

    private final void initBottomPanel()
    {
        bottom.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        bottom.add(initAddTicketButton());
        bottom.add(initPrintAllButton());
    }

    private GridBagConstraints bottomLayout()
    {
        final GridBagConstraints bottomLayout = new GridBagConstraints();
        bottomLayout.anchor = GridBagConstraints.SOUTH;
        bottomLayout.gridx = 0;
        bottomLayout.gridy = 1;
        return bottomLayout;
    }

    private GridBagConstraints scrollPaneLayout()
    {
        final GridBagConstraints scrollPaneLayout = new GridBagConstraints();
        scrollPaneLayout.anchor = GridBagConstraints.NORTHWEST;
        scrollPaneLayout.insets = new Insets(2, 2, 5, 2);
        scrollPaneLayout.fill = GridBagConstraints.BOTH;
        scrollPaneLayout.gridx = 0;
        scrollPaneLayout.gridy = 0;
        return scrollPaneLayout;
    }

    private GridBagConstraints entryListLayout()
    {
        final GridBagConstraints entryListConstraints = new GridBagConstraints();
        entryListConstraints.anchor = GridBagConstraints.NORTH;
        entryListConstraints.fill = GridBagConstraints.HORIZONTAL;
        entryListConstraints.insets = new Insets(0, 0, 5, 0);
        entryListConstraints.gridx = 0;
        entryListConstraints.gridy = 0;
        return entryListConstraints;
    }

    private void setMainLayout()
    {
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {175, 0};
        gridBagLayout.rowHeights = new int[] {101, 30, 0};
        gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
    }

    private JButton initPrintAllButton()
    {
        return new JButton(new AbstractAction("Alle drucken")
        {
            private static final long serialVersionUID = 6508625854759145758L;

            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                new PrintWorker(getAttendeeEntries()).execute();
            }
        });
    }

    public final void loadFromDb()
    {
        EventQueue.invokeLater(() ->
        {
            LOG.info("Loading attendees from database");
            final List<Ticket> tickets = new TicketRepository().getAll().stream()
                    .filter(a -> Config.YEAR.equals(a.getYear()))
                    .collect(Collectors.toList());
            for (final Ticket ticket : tickets)
            {
                final AttendeeEntry entry1 = new AttendeeEntry(ticket, getAttendeeEntries().size() + 1);
                entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                entryList.add(entry1);
            }

            entryList.getParent().revalidate();
            entryList.getParent().repaint();
            LOG.info("Finished loading attendees from database");
        });
    }

    public final void loadEntriesFromCsv(final File csvFile)
    {
        EventQueue.invokeLater(() ->
        {
            if (!csvFile.exists())
            {
                try
                {
                    Files.createFile(csvFile.toPath());
                }
                catch (final IOException e)
                {
                    LOG.error("could not create csv file", e);
                }
            }
            final List<Ticket> tickets = CSVUtils.readFromCsv(csvFile, Ticket.class);
            for (final Ticket ticket : tickets)
            {
                final AttendeeEntry entry1 = new AttendeeEntry(ticket, getAttendeeEntries().size() + 1);
                entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                entryList.add(entry1);
            }

            entryList.getParent().revalidate();
            entryList.getParent().repaint();
        });
    }

    public void saveToDb()
    {
        final List<Ticket> collect = getAttendeeEntries().stream()
                .map(a ->
                {
                    Ticket retVal = new Ticket();
                    retVal.setName(a.getPersonName());
                    retVal.setPaid(a.isPaid());
                    retVal.setSent(a.isSent());
                    retVal.setTShirt(a.getTShirt());
                    retVal.setType(a.getType());
                    retVal.setId(a.getId());
                    return retVal;
                })
                .collect(Collectors.toList());
        new TicketRepository().save(collect);
    }

    private JButton initAddTicketButton()
    {
        return new AddTicketButton(this);
    }

    public List<AttendeeEntry> getAttendeeEntries()
    {
        return Stream.of(entryList.getComponents())
                .filter(a -> AttendeeEntry.class.isAssignableFrom(a.getClass()))
                .map(AttendeeEntry.class::cast)
                .collect(Collectors.toList());
    }

    public Panel getEntryList()
    {
        return entryList;
    }
}
