package de.robadd.festivalmanager.ui.tab;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.db.CrudRepository;
import de.robadd.festivalmanager.model.Band;
import de.robadd.festivalmanager.ui.entry.BandEntry;
import de.robadd.festivalmanager.util.CSVUtils;
import de.robadd.festivalmanager.util.Config;

public final class BandsTab extends JPanel
{
    private static final long serialVersionUID = 4786178765587213932L;
    private static final Logger LOG = LoggerFactory.getLogger(BandsTab.class);
    private List<BandEntry> bandEntries = new ArrayList<>();
    private Panel entryList = new Panel();
    private ScrollPane scrollPane = new ScrollPane();
    private JPanel bottom = new JPanel();

    public BandsTab()
    {
        setMainLayout();
        entryList.setLayout(new BoxLayout(entryList, BoxLayout.Y_AXIS));
        scrollPane.add(entryList, entryListLayout());
        add(scrollPane, scrollPaneLayout());
        add(bottom, bottomLayout());
        initBottomPanel();
        // loadEntriesFromCsv();
        loadFromDb();
    }

    /**
     * @return the entries
     */
    public List<BandEntry> getEntries()
    {
        return bandEntries;
    }

    private void initBottomPanel()
    {
        bottom.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        bottom.add(initAddTicketButton());
        bottom.add(initJsonButton());
    }

    private JButton initJsonButton()
    {
        return new JButton(new AbstractAction("JSON")
        {
            private static final long serialVersionUID = 7288340044341573533L;

            @Override
            public void actionPerformed(final ActionEvent arg0)
            {
                String s = getEntries().stream().map(BandEntry::toJson).collect(Collectors.joining(","));
                StringSelection stringSelection = new StringSelection("[" + s + "]");
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
    }

    private GridBagConstraints bottomLayout()
    {
        GridBagConstraints bottomLayout = new GridBagConstraints();
        bottomLayout.anchor = GridBagConstraints.SOUTH;
        bottomLayout.gridx = 0;
        bottomLayout.gridy = 1;
        return bottomLayout;
    }

    private GridBagConstraints scrollPaneLayout()
    {
        GridBagConstraints scrollPaneLayout = new GridBagConstraints();
        scrollPaneLayout.anchor = GridBagConstraints.NORTHWEST;
        scrollPaneLayout.insets = new Insets(2, 2, 5, 2);
        scrollPaneLayout.fill = GridBagConstraints.BOTH;
        scrollPaneLayout.gridx = 0;
        scrollPaneLayout.gridy = 0;
        return scrollPaneLayout;
    }

    private GridBagConstraints entryListLayout()
    {
        GridBagConstraints entryListConstraints = new GridBagConstraints();
        entryListConstraints.anchor = GridBagConstraints.NORTH;
        entryListConstraints.fill = GridBagConstraints.HORIZONTAL;
        entryListConstraints.insets = new Insets(0, 0, 5, 0);
        entryListConstraints.gridx = 0;
        entryListConstraints.gridy = 0;
        return entryListConstraints;
    }

    private void setMainLayout()
    {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {175, 0};
        gridBagLayout.rowHeights = new int[] {101, 30, 0};
        gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
    }

    public final void loadFromDb()
    {

        EventQueue.invokeLater(() ->
        {
            List<Band> bands = CrudRepository.of(Band.class).getAll().stream().collect(Collectors.toList());
            for (Band band : bands)
            {
                BandEntry bandEntry = new BandEntry(band, bandEntries.size() + 1);
                bandEntry.setAlignmentY(Component.TOP_ALIGNMENT);
                bandEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
                entryList.add(bandEntry);
                bandEntries.add(bandEntry);
            }
            entryList.getParent().revalidate();
            entryList.getParent().repaint();
        });
    }

    public final void loadEntriesFromCsv()
    {
        String bandsCsvFile = Config.getInstance().getBandsCsvFile();
        if (bandsCsvFile == null)
        {
            return;
        }
        File csvFile = new File(bandsCsvFile);
        loadEntriesFromCsv(csvFile);
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
                catch (IOException e)
                {
                    LOG.error("Could not write csv file", e);
                }
            }
            List<Band> tickets = CSVUtils.readFromCsv(csvFile, Band.class);
            for (Band ticket : tickets)
            {
                BandEntry entry1 = new BandEntry(ticket, bandEntries.size() + 1);
                entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                entryList.add(entry1);
                bandEntries.add(entry1);
            }

            entryList.getParent().revalidate();
            entryList.getParent().repaint();
        });
    }

    private JButton initAddTicketButton()
    {
        JButton addTicketButton = new JButton(new AbstractAction("+")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                EventQueue.invokeLater(() ->
                {
                    BandEntry entry1 = new BandEntry(bandEntries.size() + 1);
                    entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                    entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                    bandEntries.add(entry1);
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
