package de.robadd.festivalmanager.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StatisticsEntry extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JLabel descriptionField;
	private JTextField valueField;
	private JTextField percentageField;

	public StatisticsEntry(final String key, final Integer value, final Float percentage)
	{
		setLayout(frameLayout());

		descriptionField = new JLabel(key);
		valueField = new JTextField();
		valueField.setEditable(false);
		percentageField = new JTextField();
		percentageField.setEditable(false);

		setDescription(key);
		setValue(value);
		setPercentage(percentage);

		GridBagConstraints gbc_descriptionField = new GridBagConstraints();
		gbc_descriptionField.anchor = GridBagConstraints.NORTH;
		add(descriptionField, gbc_descriptionField);
		GridBagConstraints gbc_valueField = new GridBagConstraints();
		gbc_valueField.anchor = GridBagConstraints.NORTH;
		gbc_valueField.fill = GridBagConstraints.HORIZONTAL;
		add(valueField, gbc_valueField);
		GridBagConstraints gbc_percentageField = new GridBagConstraints();
		gbc_percentageField.anchor = GridBagConstraints.NORTH;
		gbc_percentageField.fill = GridBagConstraints.HORIZONTAL;
		add(percentageField, gbc_percentageField);
	}

//	private GridBagConstraints labelLayout(final int col)
//	{
//		GridBagConstraints labelLayout = new GridBagConstraints();
//		labelLayout.anchor = GridBagConstraints.WEST;
//		labelLayout.insets = new Insets(0, 0, 0, 5);
//		labelLayout.gridx = col;
//		labelLayout.gridy = 0;
//		return labelLayout;
//	}

	private GridBagLayout frameLayout()
	{
		GridBagLayout frameLayout = new GridBagLayout();
		frameLayout.columnWidths = new int[]
		{ 100, 100, 100, 0 };
		frameLayout.rowHeights = new int[]
		{ 20, 0 };
		frameLayout.columnWeights = new double[]
		{ 0.0, 0.0, 0.0, Double.MIN_VALUE };
		frameLayout.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		return frameLayout;
	}

	/**
	 * @param description the key to set
	 */
	public void setDescription(final String description)
	{
		if (descriptionField != null && description != null)
		{
			descriptionField.setText(description);
		}
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final Integer value)
	{
		if (descriptionField != null && value != null)
		{
			valueField.setText(value.toString());
		}
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(final Float percentage)
	{
		if (descriptionField != null)
		{
			if (percentage != null)
			{
				percentageField.setVisible(true);
				percentageField.setText(String.format("%2.2f %%", percentage * 100));
			}
			else
			{
				percentageField.setVisible(false);
			}
		}

	}
}
