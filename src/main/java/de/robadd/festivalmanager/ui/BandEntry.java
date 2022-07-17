package de.robadd.festivalmanager.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.robadd.festivalmanager.Band;

public class BandEntry extends JPanel
{
	private static final long serialVersionUID = -6083595259931310400L;
	private Integer position = 0;
	private JTextField personNameTextField;
	private JCheckBox liveCheckbox;
	private JComboBox<String> sentCheckbox;
	private JTextField fromTextField;
	private JTextField toTextField;
	private JLabel pos;

	public BandEntry(final Band band, final int pos)
	{
		this();
		position = pos;
		personNameTextField.setText(band.getName());
		fromTextField.setText(band.getFrom().getHour() + ":" + band.getFrom().getMinute());
		toTextField.setText(band.getTo().getHour() + ":" + band.getTo().getMinute());
		liveCheckbox.setSelected(band.isLive());

		this.pos.setText(position.toString());
	}

	public BandEntry()
	{
		setPreferredSize(new Dimension(900, 20));
		setMaximumSize(new Dimension(900, 20));

		ButtonGroup type = new ButtonGroup();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 20, 131, 131, 131, 90, 90, 131 };
		gridBagLayout.rowHeights = new int[]
		{ 20, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		pos = new JLabel(position.toString());
		personNameTextField = new JTextField(10);
		personNameTextField.setPreferredSize(new Dimension(100, 20));
		personNameTextField.setMaximumSize(new Dimension(100, 20));
		personNameTextField.setHorizontalAlignment(SwingConstants.LEFT);

		fromTextField = new JTextField(10);
		fromTextField.setPreferredSize(new Dimension(100, 20));
		fromTextField.setMaximumSize(new Dimension(100, 20));
		fromTextField.setHorizontalAlignment(SwingConstants.LEFT);

		toTextField = new JTextField(10);
		toTextField.setPreferredSize(new Dimension(100, 20));
		toTextField.setMaximumSize(new Dimension(100, 20));
		toTextField.setHorizontalAlignment(SwingConstants.LEFT);

		liveCheckbox = new JCheckBox("live");
		liveCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

		sentCheckbox = new JComboBox<>();
		sentCheckbox.setModel(new DefaultComboBoxModel(new String[]
		{ "Donnerstag", "Freitag", "Samstag" }));

		final GridBagConstraints layout0 = layout(0);
		final GridBagConstraints layout1 = layoutFill(1);
		final GridBagConstraints layout2 = layout(2);
		final GridBagConstraints layout3 = layout(3);
		final GridBagConstraints layout4 = layoutFill(4);
		final GridBagConstraints layout5 = layoutFill(5);

		add(pos, layout0);
		add(personNameTextField, layout1);
		add(liveCheckbox, layout2);
		add(sentCheckbox, layout3);
		add(fromTextField, layout4);
		add(toTextField, layout5);
	}

	private static GridBagConstraints layoutFill(final int x)
	{
		GridBagConstraints personNameTextFieldLayout = new GridBagConstraints();
		personNameTextFieldLayout.fill = GridBagConstraints.BOTH;
		personNameTextFieldLayout.gridx = x;
		personNameTextFieldLayout.gridy = 0;
		return personNameTextFieldLayout;
	}

	private static GridBagConstraints layout(final int x)
	{
		GridBagConstraints typePanelLayout = new GridBagConstraints();
		typePanelLayout.anchor = GridBagConstraints.WEST;
		typePanelLayout.fill = GridBagConstraints.VERTICAL;
		typePanelLayout.gridx = x;
		typePanelLayout.gridy = 0;
		return typePanelLayout;
	}

	public String getPersonName()
	{
		return personNameTextField.getText();
	}

	public Boolean getTShirt()
	{
		return liveCheckbox.isSelected();
	}

	public String toCsvLine()
	{
		return "";
//		return new Band(
//				getPersonName(),
//				getTShirt(),
//				paidCheckbox.isSelected(),
//				sentCheckbox.isSelected()).toCsv();
	}
}
