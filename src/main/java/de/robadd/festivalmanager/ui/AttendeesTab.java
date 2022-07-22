package de.robadd.festivalmanager.ui;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.CSVUtils;
import de.robadd.festivalmanager.Config;
import de.robadd.festivalmanager.Ticket;

public final class AttendeesTab extends JPanel
{
    private static final long serialVersionUID = 2113186135796490710L;
    private static final Logger LOG = LoggerFactory.getLogger(AttendeesTab.class);
    private final List<AttendeeEntry> attendeeEntries = new ArrayList<>();
    private final Panel entryList = new Panel();
    private final ScrollPane scrollPane = new ScrollPane();
    private final JPanel bottom = new JPanel();

    /**
     * @return the entries
     */
    public List<AttendeeEntry> getEntries()
    {
        return attendeeEntries;
    }

    public AttendeesTab()
    {
        setMainLayout();
        entryList.setLayout(new BoxLayout(entryList, BoxLayout.Y_AXIS));
        scrollPane.add(entryList, entryListLayout());
        add(scrollPane, scrollPaneLayout());
        add(bottom, bottomLayout());
        initBottomPanel();
        loadEntriesFromCsv();
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
                attendeeEntries.stream().filter(AttendeeEntry::isPaid).forEach(AttendeeEntry::print);
            }
        });
    }

    final void loadEntriesFromCsv()
    {
        EventQueue.invokeLater(() ->
        {
            final File csvFile = new File(Config.getInstance().getAttendeesCsvFile());
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
                final AttendeeEntry entry1 = new AttendeeEntry(ticket, attendeeEntries.size() + 1);
                entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                entryList.add(entry1);
                attendeeEntries.add(entry1);
            }

            entryList.getParent().revalidate();
            entryList.getParent().repaint();
        });
    }

    private JButton initAddTicketButton()
    {
        final JButton addTicketButton = new JButton(new AbstractAction("+")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                EventQueue.invokeLater(() ->
                {
                    final AttendeeEntry entry1 = new AttendeeEntry(attendeeEntries.size() + 1);
                    entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                    entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                    attendeeEntries.add(entry1);
                    entryList.add(entry1);
                    entryList.getParent().revalidate();
                    entryList.getParent().repaint();
                });
            }
        });
        addTicketButton.setVerticalAlignment(SwingConstants.BOTTOM);
        addTicketButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        addTicketButton.setHorizontalAlignment(SwingConstants.LEFT);
        return addTicketButton;
    }

}
