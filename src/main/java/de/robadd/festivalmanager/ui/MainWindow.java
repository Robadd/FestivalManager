package de.robadd.festivalmanager.ui;

import static javax.swing.SwingConstants.TOP;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.robadd.festivalmanager.CSVUtils;
import de.robadd.festivalmanager.Config;
import de.robadd.festivalmanager.Ticket;

public class MainWindow
{

	private JFrame frame;
	private List<Entry> entries = new ArrayList<>();

	/**
	 * @return the entries
	 */
	public List<Entry> getEntries()
	{
		return entries;
	}

	/**
	 * Launch the application.
	 *
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 *
	 * @throws Exception
	 */
	public static void main() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(() ->
		{
			try
			{
				MainWindow window = new MainWindow();
				window.frame.setVisible(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 *
	 * @wbp.parser.entryPoint
	 */
	public MainWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 504);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("RDH Festivalmanager");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 434, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 141, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 0.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		ScrollPane scrollPane = new ScrollPane();
		GridBagConstraints scrollPaneLayout = new GridBagConstraints();
		scrollPaneLayout.anchor = GridBagConstraints.NORTHWEST;
		scrollPaneLayout.insets = new Insets(2, 2, 5, 2);
		scrollPaneLayout.fill = GridBagConstraints.BOTH;
		scrollPaneLayout.gridx = 0;
		scrollPaneLayout.gridy = 1;

		Panel entryList = new Panel();

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
		bottomLayout.gridy = 3;

		GridBagConstraints menuBarLayout = new GridBagConstraints();
		menuBarLayout.fill = GridBagConstraints.HORIZONTAL;
		menuBarLayout.insets = new Insets(0, 0, 5, 0);
		menuBarLayout.gridx = 0;
		menuBarLayout.gridy = 0;
		frame.getContentPane().add(menuBar(entryList), menuBarLayout);

		frame.getContentPane().add(bottom, bottomLayout);
		bottom.add(addTicketButton(entryList));

		JButton btnNewButton = new JButton(new AbstractAction("Alle drucken")
		{

			@Override
			public void actionPerformed(final ActionEvent arg0)
			{
				entries.stream().filter(Entry::isPaid).forEach(Entry::print);
			}

		});
		bottom.add(btnNewButton);

		loadEntriesFromCsv(entryList);

		JTabbedPane tabbedPane = new JTabbedPane(TOP);

		tabbedPane.addChangeListener(arg0 ->
		{
			Component component = tabbedPane.getComponent(tabbedPane.getSelectedIndex());
			if (component instanceof TabbedOnChangeListener)
			{
				((TabbedOnChangeListener) component).focusChanged(this);
			}
		});

		GridBagConstraints tabbedPaneLayout = new GridBagConstraints();
		tabbedPaneLayout.insets = new Insets(0, 0, 5, 0);
		tabbedPaneLayout.fill = GridBagConstraints.BOTH;
		tabbedPaneLayout.gridy = 2;
		tabbedPaneLayout.gridx = 0;
		tabbedPane.add(scrollPane, scrollPaneLayout);
		tabbedPane.setTitleAt(0, "Teilnehmer");
		tabbedPane.setEnabledAt(0, true);
		frame.getContentPane().add(tabbedPane, tabbedPaneLayout);

		StatisticsTab panel = new StatisticsTab();
		tabbedPane.addTab("Statistik", null, panel, null);

	}

	private JPanel menuBar(final Panel entryList)
	{
		JPanel menuBar = new JPanel();
		menuBar.add(loadMenu(entryList));
		menuBar.add(saveMenu());
		menuBar.add(new JButton(settingsAction()));
		return menuBar;
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
					Entry entry1 = new Entry();
					entry1.setAlignmentY(Component.TOP_ALIGNMENT);
					entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
					entries.add(entry1);
					entryList.add(entry1);
					frame.revalidate();
					frame.repaint();
				});
			}
		});
		addTicketButton.setVerticalAlignment(SwingConstants.BOTTOM);
		addTicketButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		addTicketButton.setHorizontalAlignment(SwingConstants.LEFT);
		return addTicketButton;
	}

	private JButton saveMenu()
	{
		return new JButton(new AbstractAction("Speichern")
		{
			private static final long serialVersionUID = -7146196927445858557L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				CSVUtils.writeTicketEntriesToCsv(entries, new File(Config.getInstance().getCsvFile()));
			}
		});
	}

	private JButton loadMenu(final Panel entryList)
	{
		return new JButton(new AbstractAction("Laden")
		{
			private static final long serialVersionUID = -7159226799943413043L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				loadEntriesFromCsv(entryList);
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
				Entry entry1 = new Entry(ticket, entries.size() + 1);
				entry1.setAlignmentY(Component.TOP_ALIGNMENT);
				entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
				entryList.add(entry1);
				entries.add(entry1);
			}

			entryList.revalidate();
			entryList.repaint();
		});
	}

	private AbstractAction settingsAction()
	{
		return new AbstractAction("Einstellungen")
		{
			private static final long serialVersionUID = 1005967898920124523L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				EventQueue.invokeLater(() ->
				{
					JFrame settings = new Settings();
					settings.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					settings.pack();
					settings.setLocationByPlatform(true);
					settings.setVisible(true);
					settings.setResizable(false);
				});

			}
		};
	}

}
