package de.robadd.festivalmanager.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SettingsEntry extends JPanel
{
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
	private JLabel label;
	private JTextField textField;

	public SettingsEntry(final String key, final String value)
	{
		setKey(key);
		setValue(value);
		setLayout(frameLayout());

		label = new JLabel(key);
		label.setHorizontalAlignment(SwingConstants.LEFT);

		textField = new JTextField(value);
		textField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(final KeyEvent arg0)
			{
				JTextField entry = (JTextField) arg0.getComponent();
				setValue(entry.getText());
			}
		});
		textField.setColumns(10);

		add(label, labelLayout());
		add(textField, textFieldLayout());
	}

	private GridBagConstraints labelLayout()
	{
		GridBagConstraints labelLayout = new GridBagConstraints();
		labelLayout.anchor = GridBagConstraints.WEST;
		labelLayout.insets = new Insets(0, 0, 0, 5);
		labelLayout.gridx = 0;
		labelLayout.gridy = 0;
		return labelLayout;
	}

	private GridBagConstraints textFieldLayout()
	{
		GridBagConstraints textFieldLayout = new GridBagConstraints();
		textFieldLayout.anchor = GridBagConstraints.NORTH;
		textFieldLayout.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayout.gridx = 1;
		textFieldLayout.gridy = 0;
		return textFieldLayout;
	}

	private GridBagLayout frameLayout()
	{
		GridBagLayout frameLayout = new GridBagLayout();
		frameLayout.columnWidths = new int[]
		{ 70, 440, 0 };
		frameLayout.rowHeights = new int[]
		{ 20, 0 };
		frameLayout.columnWeights = new double[]
		{ 0.0, 1.0, Double.MIN_VALUE };
		frameLayout.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		return frameLayout;
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(final String key)
	{
		this.key = key;
		if (label != null)
		{
			label.setText(key);
		}
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
		if (label != null)
		{
			textField.setText(value);
		}
	}
}
