package de.robadd.festivalmanager.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import de.robadd.festivalmanager.Band;
import de.robadd.festivalmanager.CSVWritable;
import de.robadd.festivalmanager.FestivalDay;

public class BandEntry extends JPanel implements CSVWritable
{
	private static final long serialVersionUID = -6083595259931310400L;
	private Integer position = 0;
	private JTextField bandNameTextField;
	private JCheckBox liveCheckbox;
	private JComboBox<FestivalDay> dayBox;
	private JTextField fromHourTextField;
	private JTextField fromMinuteTextField;
	private JTextField toHourTextField;
	private JTextField toMinuteTextField;
	private JLabel pos;

	public BandEntry(final int pos)
	{
		this();
		position = pos;
		this.pos.setText(position.toString());
	}

	public BandEntry(final Band band, final int pos)
	{
		this(pos);
		bandNameTextField.setText(band.getName());
		fromHourTextField.setText(Integer.toString(band.getFrom().getHour()));
		fromMinuteTextField.setText(Integer.toString(band.getFrom().getMinute()));
		toHourTextField.setText(Integer.toString(band.getTo().getHour()));
		toMinuteTextField.setText(Integer.toString(band.getTo().getMinute()));
		liveCheckbox.setSelected(band.isLive());
	}

	public BandEntry()
	{
		setPreferredSize(new Dimension(900, 20));
		setMaximumSize(new Dimension(900, 20));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 20, 130, 50, 50, 60, 60, 30, 60, 60 };
		gridBagLayout.rowHeights = new int[]
		{ 20, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		pos = new JLabel(position.toString());
		bandNameTextField = new JTextField(20);
		bandNameTextField.setPreferredSize(new Dimension(100, 20));
		bandNameTextField.setMaximumSize(new Dimension(100, 20));
		bandNameTextField.setHorizontalAlignment(SwingConstants.LEFT);

		fromHourTextField = new JTextField(5);
		fromHourTextField.setPreferredSize(new Dimension(50, 20));
		fromHourTextField.setMaximumSize(new Dimension(50, 20));
		fromHourTextField.setHorizontalAlignment(SwingConstants.LEFT);

		fromMinuteTextField = new JTextField(10);
		fromMinuteTextField.setPreferredSize(new Dimension(100, 20));
		fromMinuteTextField.setMaximumSize(new Dimension(100, 20));
		fromMinuteTextField.setHorizontalAlignment(SwingConstants.LEFT);

		toHourTextField = new JTextField(10);
		toHourTextField.setPreferredSize(new Dimension(100, 20));
		toHourTextField.setMaximumSize(new Dimension(100, 20));
		toHourTextField.setHorizontalAlignment(SwingConstants.LEFT);

		toMinuteTextField = new JTextField(10);
		toMinuteTextField.setPreferredSize(new Dimension(100, 20));
		toMinuteTextField.setMaximumSize(new Dimension(100, 20));
		toMinuteTextField.setHorizontalAlignment(SwingConstants.LEFT);

		liveCheckbox = new JCheckBox("live");
		liveCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

		dayBox = new JComboBox<>();
		dayBox.setModel(new DefaultComboBoxModel<>(FestivalDay.values()));

		final GridBagConstraints layout0 = layoutFill(0);
		final GridBagConstraints layout1 = layoutFill(1);
		final GridBagConstraints layout2 = layout(2);
		final GridBagConstraints layout3 = layout(3);
		final GridBagConstraints layout4 = layoutFill(4);
		final GridBagConstraints layout5 = layoutFill(5);
		final GridBagConstraints layout6 = layoutFill(6);
		final GridBagConstraints layout7 = layoutFill(7);
		final GridBagConstraints layout8 = layoutFill(8);

		add(pos, layout0);
		add(bandNameTextField, layout1);
		add(liveCheckbox, layout2);
		add(dayBox, layout3);
		add(fromHourTextField, layout4);
		add(fromMinuteTextField, layout5);
		add(new JLabel("bis"), layout6);
		add(toHourTextField, layout7);
		add(toMinuteTextField, layout8);
	}

	private static GridBagConstraints layoutFill(final int x)
	{
		GridBagConstraints personNameTextFieldLayout = new GridBagConstraints();
		personNameTextFieldLayout.anchor = GridBagConstraints.EAST;
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
		return bandNameTextField.getText();
	}

	public Boolean getTShirt()
	{
		return liveCheckbox.isSelected();
	}

	private LocalDateTime getFrom()
	{
		FestivalDay selectedItem = (FestivalDay) dayBox.getSelectedItem();
		return LocalDateTime.of(selectedItem.getYear(), selectedItem.getMonth(), selectedItem.getDay(), Integer.valueOf(
			fromHourTextField.getText()), Integer.valueOf(fromMinuteTextField.getText()));
	}

	private LocalDateTime getTo()
	{
		FestivalDay selectedItem = (FestivalDay) dayBox.getSelectedItem();
		return LocalDateTime.of(selectedItem.getYear(), selectedItem.getMonth(), selectedItem.getDay(), Integer.valueOf(
			toHourTextField.getText()), Integer.valueOf(toMinuteTextField.getText()));
	}

	public String toCsvLine()
	{

		return new Band(getFrom(), getTo(), bandNameTextField.getText(), liveCheckbox
				.isSelected()).toCsv();
	}

	public String toJson()
	{
		return new Band(getFrom(), getTo(), bandNameTextField.getText(), liveCheckbox
				.isSelected()).toJson();
	}

	@Override
	public void fillfromCsv(final String line)
	{
//		bandNameTextField.setText(band.getName());
//		fromHourTextField.setText(Integer.toString(band.getFrom().getHour()));
//		fromMinuteTextField.setText(Integer.toString(band.getFrom().getMinute()));
//		toHourTextField.setText(Integer.toString(band.getTo().getHour()));
//		toMinuteTextField.setText(Integer.toString(band.getTo().getMinute()));
//		liveCheckbox.setSelected(band.isLive());
	}

	@Override
	public String toCsv()
	{
		return toCsvLine();
	}
}
