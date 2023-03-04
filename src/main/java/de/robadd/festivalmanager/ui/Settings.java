package de.robadd.festivalmanager.ui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.ui.entry.SettingsEntry;
import de.robadd.festivalmanager.util.Config;

public class Settings extends JFrame
{
    private static final long serialVersionUID = 3774289668188779225L;
    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);
    private List<SettingsEntry> settingsEntries = new ArrayList<>();

    public Settings()
    {

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {434, 0};
        gridBagLayout.rowHeights = new int[] {261, 30, 0};
        gridBagLayout.columnWeights = new double[] {0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {1.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        GridBagConstraints contentPaneLayout = new GridBagConstraints();
        contentPaneLayout.insets = new Insets(0, 0, 5, 0);
        contentPaneLayout.fill = GridBagConstraints.BOTH;
        contentPaneLayout.gridx = 0;
        contentPaneLayout.gridy = 0;
        getContentPane().add(contentPane, contentPaneLayout);

        JButton btnNewButton = new JButton(new AbstractAction("Speichern")
        {
            private static final long serialVersionUID = 1181559014714424282L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {

                EventQueue.invokeLater(() ->
                {
                    try
                    {
                        settingsEntries.stream().forEach(a -> Config.getInstance().setParam(a.getKey(), a.getValue()));
                        Config.getInstance().saveIni();
                    }
                    catch (IOException e1)
                    {
                        LOG.error("Could not save config", e1);
                    }
                });

            }
        });
        GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.gridx = 0;
        buttonLayout.gridy = 1;
        getContentPane().add(btnNewButton, buttonLayout);

        for (Entry<String, String> entry : Config.getInstance().getMap().entrySet())
        {
            SettingsEntry settingsEntry = new SettingsEntry(entry.getKey(), entry.getValue());
            settingsEntries.add(settingsEntry);
            contentPane.add(settingsEntry);
        }
    }

}
