package de.robadd.festivalmanager.ui;

import static javax.swing.SwingConstants.TOP;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow
{
	private JFrame frame;
	private JTabbedPane tabbedPane;

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
		GridBagLayout contentLayout = new GridBagLayout();
		contentLayout.columnWidths = new int[]
		{ 434, 0 };
		contentLayout.rowHeights = new int[]
		{ 0, 0, 141, 0 };
		contentLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		contentLayout.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, 0.0 };
		frame.getContentPane().setLayout(contentLayout);

		GridBagConstraints menuBarLayout = new GridBagConstraints();
		menuBarLayout.fill = GridBagConstraints.HORIZONTAL;
		menuBarLayout.insets = new Insets(0, 0, 5, 0);
		menuBarLayout.gridx = 0;
		menuBarLayout.gridy = 0;

		GridBagConstraints tabbedPaneLayout = new GridBagConstraints();
		tabbedPaneLayout.insets = new Insets(0, 0, 5, 0);
		tabbedPaneLayout.fill = GridBagConstraints.BOTH;
		tabbedPaneLayout.gridy = 2;
		tabbedPaneLayout.gridx = 0;

		AttendeesTab attendeesPane = new AttendeesTab();
		initTabbedPane(attendeesPane);
		BandEntry bandEntry = new BandEntry();
		StatisticsTab panel = new StatisticsTab();

		tabbedPane.addTab("Teilnehmer", null, attendeesPane, null);
		tabbedPane.addTab("Statistik", null, panel, null);
		tabbedPane.addTab("Bands", null, bandEntry, null);

		frame.getContentPane().add(tabbedPane, tabbedPaneLayout);
		frame.getContentPane().add(menuBar(null), menuBarLayout);
	}

	private void initTabbedPane(final AttendeesTab attendeesPane)
	{
		tabbedPane = new JTabbedPane(TOP);
		tabbedPane.addChangeListener(arg0 ->
		{
			Component component = tabbedPane.getComponent(tabbedPane.getSelectedIndex());
			if (component instanceof TabbedOnChangeListener)
			{
				((TabbedOnChangeListener) component).focusChanged(attendeesPane);
			}
		});
	}

	private JPanel menuBar(final Panel entryList)
	{
		JPanel menuBar = new JPanel();
		menuBar.add(loadMenu(entryList));
		menuBar.add(saveMenu());
		menuBar.add(new JButton(settingsAction()));
		return menuBar;
	}

	private JButton saveMenu()
	{
		return new JButton(new AbstractAction("Speichern")
		{
			private static final long serialVersionUID = -7146196927445858557L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				// CSVUtils.writeTicketEntriesToCsv(attendeeEntries, new
				// File(Config.getInstance().getCsvFile()));
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
				// loadEntriesFromCsv(entryList);
			}

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
