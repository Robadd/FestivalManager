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

import de.robadd.festivalmanager.CSVUtils;
import de.robadd.festivalmanager.Config;
import de.robadd.festivalmanager.Ticket;

public class AttendeesTab extends JPanel
{
	private static final long serialVersionUID = 2113186135796490710L;
	private List<AttendeeEntry> attendeeEntries = new ArrayList<>();
	private Panel entryList = new Panel();
	private ScrollPane scrollPane = new ScrollPane();
	private JPanel bottom = new JPanel();

	private JButton btnNewButton;
	private JButton addTicketButton;

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
		initPrintAllButton();
		initAddTicketButton();
		initBottomPanel();
		loadEntriesFromCsv();
	}

	private void initBottomPanel()
	{
		bottom.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		bottom.add(addTicketButton);
		bottom.add(btnNewButton);
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
		gridBagLayout.columnWidths = new int[]
		{ 175, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 101, 30, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
	}

	private void initPrintAllButton()
	{
		btnNewButton = new JButton(new AbstractAction("Alle drucken")
		{
			private static final long serialVersionUID = 6508625854759145758L;

			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				attendeeEntries.stream().filter(AttendeeEntry::isPaid).forEach(AttendeeEntry::print);
			}
		});
	}

	void loadEntriesFromCsv()
	{
		EventQueue.invokeLater(() ->
		{
			File csvFile = new File(Config.getInstance().getCsvFile());
			if (!csvFile.exists())
			{
				try
				{
					Files.createFile(csvFile.toPath());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			List<Ticket> tickets = CSVUtils.readCsvToTickets(csvFile);
			for (Ticket ticket : tickets)
			{
				AttendeeEntry entry1 = new AttendeeEntry(ticket, attendeeEntries.size() + 1);
				entry1.setAlignmentY(Component.TOP_ALIGNMENT);
				entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
				entryList.add(entry1);
				attendeeEntries.add(entry1);
			}

			entryList.getParent().revalidate();
			entryList.getParent().repaint();
		});
	}

	private void initAddTicketButton()
	{
		addTicketButton = new JButton(new AbstractAction("+")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				EventQueue.invokeLater(() ->
				{
					AttendeeEntry entry1 = new AttendeeEntry(attendeeEntries.size() + 1);
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
	}

}
