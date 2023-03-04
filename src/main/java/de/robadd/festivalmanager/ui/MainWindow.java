package de.robadd.festivalmanager.ui;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import static javax.swing.SwingConstants.TOP;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.Main;
import de.robadd.festivalmanager.ui.tab.AttendeesTab;
import de.robadd.festivalmanager.ui.tab.BandsTab;
import de.robadd.festivalmanager.ui.tab.StatisticsTab;

public class MainWindow
{
    private static final Logger LOG = LoggerFactory.getLogger(MainWindow.class);
    private static MainWindow instance;
    private final JFrame frame = new JFrame();
    private final JTabbedPane tabbedPane = new JTabbedPane(TOP);
    private AttendeesTab attendeesTab = new AttendeesTab();
    private BandsTab bandsTab = new BandsTab();
    private StatisticsTab settingsTab = new StatisticsTab();
    private StatusBar statusBar;

    public static MainWindow getInstance()
    {
        return instance;
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
    public static void main() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        EventQueue.invokeLater(() ->
        {
            try
            {
                MainWindow.instance = new MainWindow();
                MainWindow.getInstance().getFrame().setVisible(true);
                addConfirmOnCloseDialog();
            }
            catch (final Exception e)
            {
                LOG.error("Exception", e);
            }
        });
    }

    private static void addConfirmOnCloseDialog()
    {
        MainWindow.getInstance().getFrame().setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        MainWindow.getInstance().getFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(final WindowEvent e)
            {
                if (!Main.isDirty())
                {
                    System.exit(0);
                }
                final int result = JOptionPane.showConfirmDialog(MainWindow.getInstance().getFrame(), "Speichern ?",
                    "Beenden",
                    YES_NO_CANCEL_OPTION, QUESTION_MESSAGE);
                switch (result)
                {
                case JOptionPane.CANCEL_OPTION:
                    break;
                case JOptionPane.YES_OPTION:
                    MainWindow.getInstance().getStatusBar().setStatus("Speichern");
                    MainWindow.getInstance().getStatusBar().setActiveWithoutValue();
                    MainWindow.getAttendeesTab().saveToDb();
                    MainWindow.getInstance().getStatusBar().reset();
                    MainWindow.getInstance().getStatusBar().resetStatus();
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                default:
                    System.exit(0);
                    break;
                }
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
        final GridBagConstraints menuBarLayout = new GridBagConstraints();
        menuBarLayout.fill = GridBagConstraints.HORIZONTAL;
        menuBarLayout.insets = new Insets(0, 0, 5, 0);
        menuBarLayout.gridx = 0;
        menuBarLayout.gridy = 0;

        final GridBagConstraints tabbedPaneLayout = new GridBagConstraints();
        tabbedPaneLayout.insets = new Insets(0, 0, 5, 0);
        tabbedPaneLayout.fill = GridBagConstraints.BOTH;
        tabbedPaneLayout.gridy = 2;
        tabbedPaneLayout.gridx = 0;

        final GridBagConstraints statusLayout = new GridBagConstraints();
        statusLayout.insets = new Insets(0, 0, 5, 0);
        statusLayout.fill = GridBagConstraints.BOTH;
        statusLayout.gridy = 3;
        statusLayout.gridx = 0;

        initTabbedPane();

        tabbedPane.addTab("Teilnehmer", null, attendeesTab, null);
        tabbedPane.addTab("Statistik", null, settingsTab, null);
        tabbedPane.addTab("Bands", null, bandsTab, null);

        getFrame().getContentPane().add(tabbedPane, tabbedPaneLayout);
        getFrame().getContentPane().add(menuBar(), menuBarLayout);
        statusBar = new StatusBar();
        getFrame().getContentPane().add(getStatusBar(), statusLayout);
    }

    private void initMainFrame()
    {
        getFrame().setBounds(100, 100, 950, 504);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setTitle("RDH Festivalmanager");
        final GridBagLayout contentLayout = new GridBagLayout();
        contentLayout.columnWidths = new int[] {434, 0};
        contentLayout.rowHeights = new int[] {0, 0, 141, 0};
        contentLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        contentLayout.rowWeights = new double[] {0.0, 0.0, 1.0, 0.0};
        getFrame().getContentPane().setLayout(contentLayout);
    }

    private void initTabbedPane()
    {
        tabbedPane.addChangeListener(arg0 ->
        {
            final Component component = tabbedPane.getComponent(tabbedPane.getSelectedIndex());
            if (component instanceof TabbedOnChangeListener)
            {
                ((TabbedOnChangeListener) component).focusChanged(getAttendeesTab());
            }
        });
    }

    private JPanel menuBar()
    {
        return new MenuBar();
    }

    public static AttendeesTab getAttendeesTab()
    {
        return MainWindow.getInstance().attendeesTab;
    }

    JFrame getFrame()
    {
        return frame;
    }

    public StatusBar getStatusBar()
    {
        return statusBar;
    }

}
