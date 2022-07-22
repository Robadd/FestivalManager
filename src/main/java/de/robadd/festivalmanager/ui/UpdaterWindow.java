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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.updater.Updater;

public final class UpdaterWindow
{
    private static final Logger LOG = LoggerFactory.getLogger(UpdaterWindow.class);
    private JFrame frame;
    private final List<AttendeeEntry> attendeeEntries = new ArrayList<>();

    /**
     * @return the entries
     */
    public List<AttendeeEntry> getEntries()
    {
        return attendeeEntries;
    }

    public static void main(final String newVersion) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException,
            UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(() ->
        {
            try
            {
                final UpdaterWindow window = new UpdaterWindow(newVersion);
                window.frame.setVisible(true);
            }
            catch (final Exception e)
            {
                LOG.error("Could not initalize Updater Window", e);
            }
        });
    }

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

        final GridBagLayout frameLayout = new GridBagLayout();
        frameLayout.columnWidths = new int[] {434, 0};
        frameLayout.rowHeights = new int[] {0, 141, 0, 10, 0};
        frameLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        frameLayout.rowWeights = new double[] {0.0, 1.0, 0.0, 0.0, 0.0};
        frame.getContentPane().setLayout(frameLayout);

        final JLabel newLabel = new JLabel("Neue Version Verf\u00FCgbar:");
        newLabel.setFont(UIManager.getFont("FormattedTextField.font"));

        final JPanel mainPanel = new JPanel();
        final GridBagConstraints mainPanelLayout = new GridBagConstraints();
        mainPanelLayout.insets = new Insets(5, 5, 5, 0);
        mainPanelLayout.ipady = 5;
        mainPanelLayout.ipadx = 5;
        mainPanelLayout.anchor = GridBagConstraints.WEST;
        mainPanelLayout.fill = GridBagConstraints.HORIZONTAL;
        mainPanelLayout.gridx = 0;
        mainPanelLayout.gridy = 0;

        final JLabel newVersionLabelVersion = new JLabel(newVersion);
        newVersionLabelVersion.setVerticalAlignment(SwingConstants.TOP);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        final JLabel releaseNotesLabel = new JLabel("Neuerungen:");

        final JButton installButton = new JButton("Update installieren");
        final GridBagConstraints installButtonLayout = new GridBagConstraints();
        installButtonLayout.insets = new Insets(0, 0, 5, 0);
        installButtonLayout.gridx = 0;
        installButtonLayout.gridy = 2;

        final JSeparator separator = new JSeparator();
        final GridBagConstraints separatorLayout = new GridBagConstraints();
        separatorLayout.insets = new Insets(0, 0, 5, 0);
        separatorLayout.gridx = 0;
        separatorLayout.gridy = 3;
        final Dimension d = separator.getPreferredSize();
        d.height = 10;
        separator.setPreferredSize(d);

        final JButton closeButton = new JButton(new AbstractAction("Schlie\u00DFen")
        {
            private static final long serialVersionUID = -3799989684779093484L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                frame.setVisible(false);
            }

        });
        final GridBagConstraints closeButtonLayout = new GridBagConstraints();
        closeButtonLayout.anchor = GridBagConstraints.EAST;
        closeButtonLayout.gridx = 0;
        closeButtonLayout.gridy = 4;

        mainPanel.add(newLabel);
        mainPanel.add(newVersionLabelVersion);
        mainPanel.add(releaseNotesLabel);
        frame.getContentPane().add(mainPanel, mainPanelLayout);

        final JScrollPane scrollPane = new JScrollPane();
        final GridBagConstraints scrollPaneLayout = new GridBagConstraints();
        scrollPaneLayout.fill = GridBagConstraints.BOTH;
        scrollPaneLayout.insets = new Insets(0, 5, 5, 5);
        scrollPaneLayout.gridx = 0;
        scrollPaneLayout.gridy = 1;
        frame.getContentPane().add(scrollPane, scrollPaneLayout);

        final JTextArea releaseNotesTextArea = new JTextArea();
        releaseNotesTextArea.setText(Updater.getReleaseNotes("0.1.0"));
        scrollPane.setViewportView(releaseNotesTextArea);
        releaseNotesTextArea.setEditable(false);
        releaseNotesTextArea.setLineWrap(true);

        frame.getContentPane().add(installButton, installButtonLayout);
        frame.getContentPane().add(separator, separatorLayout);
        frame.getContentPane().add(closeButton, closeButtonLayout);
    }

}
