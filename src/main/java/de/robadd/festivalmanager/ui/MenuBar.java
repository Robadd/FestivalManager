package de.robadd.festivalmanager.ui;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

class MenuBar extends JPanel
{
    private static final long serialVersionUID = -2377171868221243651L;

    MenuBar()
    {
        super();
        this.add(loadMenu());
        this.add(saveMenu());
        this.add(settingsAction());
    }

    private JButton loadMenu()
    {
        return new JButton(new AbstractAction("Importieren")
        {
            private static final long serialVersionUID = -7159226799943413043L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setFileFilter(new FileFilter()
                {

                    @Override
                    public boolean accept(final File f)
                    {
                        return f.isDirectory() || f.getName().endsWith(".csv");
                    }

                    @Override
                    public String getDescription()
                    {
                        return "CSV Datei";
                    }
                });
                final int returnVal = fc.showOpenDialog(MainWindow.getInstance().getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    final File file = fc.getSelectedFile();
                    MainWindow.getAttendeesTab().loadEntriesFromCsv(file);
                }
            }
        });
    }

    private JButton saveMenu()
    {
        return new JButton(new AbstractAction("Speichern")
        {
            private static final long serialVersionUID = -7146196927445858557L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {

                new SwingWorker<Object, Object>()
                {

                    @Override
                    protected Object doInBackground() throws Exception
                    {
                        MainWindow.getInstance().getStatusBar().setStatus("Speichern");
                        MainWindow.getInstance().getStatusBar().setActiveWithoutValue();
                        MainWindow.getAttendeesTab().saveToDb();
                        MainWindow.getInstance().getStatusBar().reset();
                        MainWindow.getInstance().getStatusBar().resetStatus();
                        return null;
                    }

                }.execute();
            }
        });
    }

    private JButton settingsAction()
    {
        return new JButton(new AbstractAction("Einstellungen")
        {
            private static final long serialVersionUID = 1005967898920124523L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                EventQueue.invokeLater(() ->
                {
                    final JFrame settings = new Settings();
                    settings.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    settings.pack();
                    settings.setLocationByPlatform(true);
                    settings.setVisible(true);
                    settings.setResizable(false);
                });

            }
        });
    }
}
