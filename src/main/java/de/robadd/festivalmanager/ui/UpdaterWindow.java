package de.robadd.festivalmanager.ui;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.robadd.festivalmanager.updater.Updater;

public class UpdaterWindow
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
	public static void main(final String newVersion) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		EventQueue.invokeLater(() ->
		{
			try
			{
				UpdaterWindow window = new UpdaterWindow(newVersion);
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
	public UpdaterWindow(final String newVersion)
	{
		initialize(newVersion);
	}

	/**
	 * Initialize the contents of the frame.
	 *
	 * @param newVersion
	 */
	private void initialize(final String newVersion)
	{

		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setTitle("Update");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 434, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 141, 0, 10, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 1.0, 0.0, 0.0, 0.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Neue Version Verf\u00FCgbar:");
		lblNewLabel.setFont(UIManager.getFont("FormattedTextField.font"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 0);
		gbc_panel.ipady = 5;
		gbc_panel.ipadx = 5;
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;

		JLabel lblNewLabel_1 = new JLabel(newVersion);
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.PAGE_START;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		gbc_lblNewLabel_1.ipadx = 5;
		gbc_lblNewLabel_1.ipady = 5;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lblNewLabel_2 = new JLabel("Neuerungen:");

		JButton btnNewButton_1 = new JButton("Update installieren");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 2;

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		Dimension d = separator.getPreferredSize();
		d.height = 10;
		separator.setPreferredSize(d);

		JButton btnNewButton = new JButton(new AbstractAction("Schlie\u00DFen")
		{
			private static final long serialVersionUID = -3799989684779093484L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				frame.setVisible(false);
			}

		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.EAST;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 4;

		panel.add(lblNewLabel);
		panel.add(lblNewLabel_1);
		panel.add(lblNewLabel_2);
		frame.getContentPane().add(panel, gbc_panel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 5, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		JTextArea txtrAAA = new JTextArea();
		txtrAAA.setText(Updater.getReleaseNotes("0.1.0"));
		scrollPane.setViewportView(txtrAAA);
		txtrAAA.setEditable(false);
		txtrAAA.setLineWrap(true);
		frame.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
		frame.getContentPane().add(separator, gbc_separator);
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
	}

}
