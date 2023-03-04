package de.robadd.festivalmanager.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusBar extends JPanel
{

    private static final long serialVersionUID = -4828201515794620335L;
    private JProgressBar progressBar;
    private JLabel lblNewLabel;

    /**
     * Create the panel.
     */
    public StatusBar()
    {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {0, 0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0};
        gridBagLayout.columnWeights = new double[] {1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        lblNewLabel = new JLabel("");
        GridBagConstraints statusLayout = new GridBagConstraints();
        statusLayout.anchor = GridBagConstraints.WEST;
        statusLayout.insets = new Insets(0, 5, 0, 0);
        statusLayout.gridx = 0;
        statusLayout.gridy = 0;
        add(lblNewLabel, statusLayout);

        progressBar = new JProgressBar();
        GridBagConstraints prograssBarLayout = new GridBagConstraints();
        prograssBarLayout.insets = new Insets(0, 0, 0, 5);
        prograssBarLayout.fill = GridBagConstraints.HORIZONTAL;
        prograssBarLayout.gridx = 1;
        prograssBarLayout.gridy = 0;
        add(progressBar, prograssBarLayout);

    }

    public void setStatus(final String status)
    {
        lblNewLabel.setText(status);
        lblNewLabel.revalidate();
        lblNewLabel.repaint();
    }

    public void resetStatus()
    {
        lblNewLabel.setText("");
        lblNewLabel.revalidate();
        lblNewLabel.repaint();
    }

    public void setMax(final int max)
    {
        progressBar.setMaximum(max);
        progressBar.revalidate();
        progressBar.repaint();
    }

    public void increment()
    {
        progressBar.setValue(progressBar.getValue() + 1);
        progressBar.revalidate();
        progressBar.repaint();
    }

    public void reset()
    {
        progressBar.setValue(0);
        progressBar.setIndeterminate(false);
        progressBar.revalidate();
        progressBar.repaint();
    }

    public void setActiveWithoutValue()
    {
        progressBar.setIndeterminate(true);
        revalidate();
        repaint();
    }

}
