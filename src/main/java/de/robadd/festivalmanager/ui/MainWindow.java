package de.robadd.festivalmanager.ui;

import static javax.swing.SwingConstants.TOP;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.robadd.festivalmanager.CSVUtils;
import de.robadd.festivalmanager.Config;

public class MainWindow
{
	private JFrame frame = new JFrame();
	private JTabbedPane tabbedPane = new JTabbedPane(TOP);
	private AttendeesTab attendeesPane = new AttendeesTab();
	private StatisticsTab settingsTab = new StatisticsTab();
	private BandsTab bandsTab = new BandsTab();
	private JPanel menuBar = new JPanel();

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
		initMainFrame();

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

		initTabbedPane();

		tabbedPane.addTab("Teilnehmer", null, attendeesPane, null);
		tabbedPane.addTab("Statistik", null, settingsTab, null);
		tabbedPane.addTab("Bands", null, bandsTab, null);

		frame.getContentPane().add(tabbedPane, tabbedPaneLayout);
		frame.getContentPane().add(menuBar(), menuBarLayout);
	}

	private void initMainFrame()
	{
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
	}

	private void initTabbedPane()
	{
		tabbedPane.addChangeListener(arg0 ->
		{
			Component component = tabbedPane.getComponent(tabbedPane.getSelectedIndex());
			if (component instanceof TabbedOnChangeListener)
			{
				((TabbedOnChangeListener) component).focusChanged(attendeesPane);
			}
		});
	}

	private JPanel menuBar()
	{
		menuBar.add(loadMenu());
		menuBar.add(saveMenu());
		menuBar.add(settingsAction());
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
				CSVUtils.writeTicketEntriesToCsv(attendeesPane.getEntries(), new File(Config.getInstance().getCsvFile()));
			}
		});
	}

	private JButton loadMenu()
	{
		return new JButton(new AbstractAction("Laden")
		{
			private static final long serialVersionUID = -7159226799943413043L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				attendeesPane.loadEntriesFromCsv();
			}

		});
	}

	private JButton settingsAction()
	{
		return new JButton(new AbstractAction("Einstellungen")
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
		});
	}
}
