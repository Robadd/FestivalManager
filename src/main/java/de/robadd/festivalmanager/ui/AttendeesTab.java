package de.robadd.festivalmanager.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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

	/**
	 * @return the entries
	 */
	public List<AttendeeEntry> getEntries()
	{
		return attendeeEntries;
	}

	public AttendeesTab()
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

		Panel entryList = new Panel();
		GridBagConstraints entryListLayout = new GridBagConstraints();
		entryListLayout.insets = new Insets(0, 0, 5, 0);
		entryListLayout.gridx = 0;
		entryListLayout.gridy = 0;
		entryListLayout.anchor = GridBagConstraints.NORTH;

		JButton btnNewButton = addButton();
		ScrollPane scrollPane = new ScrollPane();
		GridBagConstraints scrollPaneLayout = new GridBagConstraints();
		scrollPaneLayout.anchor = GridBagConstraints.NORTHWEST;
		scrollPaneLayout.insets = new Insets(2, 2, 5, 2);
		scrollPaneLayout.fill = GridBagConstraints.BOTH;
		scrollPaneLayout.gridx = 0;
		scrollPaneLayout.gridy = 0;
		GridBagConstraints entryListConstraints = new GridBagConstraints();
		entryListConstraints.anchor = GridBagConstraints.NORTH;
		entryListConstraints.fill = GridBagConstraints.HORIZONTAL;
		entryListConstraints.insets = new Insets(0, 0, 5, 0);
		entryListConstraints.gridx = 0;
		entryListConstraints.gridy = 0;
		scrollPane.add(entryList, entryListConstraints);
		entryList.setLayout(new BoxLayout(entryList, BoxLayout.Y_AXIS));
		JPanel bottom = new JPanel();
		bottom.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		FlowLayout flowLayout = (FlowLayout) bottom.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);

		GridBagConstraints bottomLayout = new GridBagConstraints();
		bottomLayout.anchor = GridBagConstraints.SOUTH;
		bottomLayout.gridx = 0;
		bottomLayout.gridy = 1;
		loadEntriesFromCsv(entryList);
		add(bottom, bottomLayout);
		bottom.add(addTicketButton(entryList));
		bottom.add(btnNewButton);

		add(scrollPane, scrollPaneLayout);

		// entryList.add(new AttendeeEntry(0));

	}

	private JButton addButton()
	{
		return new JButton(new AbstractAction("Alle drucken")
		{
			private static final long serialVersionUID = 8657185749156956538L;

			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				attendeeEntries.stream().filter(AttendeeEntry::isPaid).forEach(AttendeeEntry::print);
			}

		});
	}

	private void loadEntriesFromCsv(final Panel entryList)
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

	private JButton addTicketButton(final Panel entryList)
	{
		JButton addTicketButton = new JButton(new AbstractAction("+")
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
		return addTicketButton;
	}

}
