package de.robadd.festivalmanager.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class StatisticsEntry extends JPanel
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

        GridBagConstraints descriptionFieldLayout = new GridBagConstraints();
        descriptionFieldLayout.anchor = GridBagConstraints.NORTH;
        add(descriptionField, descriptionFieldLayout);
        GridBagConstraints valueFieldLayout = new GridBagConstraints();
        valueFieldLayout.anchor = GridBagConstraints.NORTH;
        valueFieldLayout.fill = GridBagConstraints.HORIZONTAL;
        add(valueField, valueFieldLayout);
        GridBagConstraints percentageFieldLayout = new GridBagConstraints();
        percentageFieldLayout.anchor = GridBagConstraints.NORTH;
        percentageFieldLayout.fill = GridBagConstraints.HORIZONTAL;
        add(percentageField, percentageFieldLayout);
    }

    private GridBagLayout frameLayout()
    {
        GridBagLayout frameLayout = new GridBagLayout();
        frameLayout.columnWidths = new int[] {100, 100, 100, 0};
        frameLayout.rowHeights = new int[] {20, 0};
        frameLayout.columnWeights = new double[] {0.0, 0.0, 0.0, Double.MIN_VALUE};
        frameLayout.rowWeights = new double[] {0.0, Double.MIN_VALUE};
        return frameLayout;
    }

    /**
     * @param description the key to set
     */
    public final void setDescription(final String description)
    {
        if (descriptionField != null && description != null)
        {
            descriptionField.setText(description);
        }
    }

    /**
     * @param value the value to set
     */
    public final void setValue(final Integer value)
    {
        if (descriptionField != null && value != null)
        {
            valueField.setText(value.toString());
        }
    }

    /**
     * @param percentage the percentage to set
     */
    public final void setPercentage(final Float percentage)
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
