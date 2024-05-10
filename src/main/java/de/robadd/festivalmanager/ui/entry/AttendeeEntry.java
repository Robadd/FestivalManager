package de.robadd.festivalmanager.ui.entry;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.robadd.festivalmanager.model.Ticket;
import de.robadd.festivalmanager.model.type.CSVWritable;
import de.robadd.festivalmanager.ui.MainWindow;
import de.robadd.festivalmanager.ui.StatusBar;
import de.robadd.festivalmanager.util.Config;
import de.robadd.festivalmanager.util.PDFWriter;

public final class AttendeeEntry extends JPanel implements CSVWritable
{
    private static final long serialVersionUID = -6083595259931310400L;
    private static final Logger LOG = LoggerFactory.getLogger(AttendeeEntry.class);
    private Integer position = 0;
    private JTextField personNameTextField;
    private JRadioButton type1Day;
    private JRadioButton type3Day;
    private JRadioButton typeVisitor;
    private JCheckBox tShirtCheckbox;
    private JCheckBox paidCheckbox;
    private JButton printPdfButton;
    private JCheckBox sentCheckbox;
    private Integer id;
    private Integer year = Config.YEAR;
    private JLabel pos = new JLabel();
    private ButtonGroup type;

    public AttendeeEntry(final Ticket ticket, final int pos)
    {
        this(pos);

        personNameTextField.setText(ticket.getName());
        if (ticket.getType() == 1)
        {
            type1Day.setSelected(true);
        }
        else if (ticket.getType() == 2)
        {
            type3Day.setSelected(true);
        }
        else if (ticket.getType() == 3)
        {
            typeVisitor.setSelected(true);
        }
        tShirtCheckbox.setSelected(ticket.getTShirt());
        paidCheckbox.setSelected(ticket.getPaid());
        printPdfButton.setEnabled(ticket.getPaid());
        sentCheckbox.setSelected(ticket.getSent());
        id = ticket.getId();
        if (!year.equals(ticket.getYear()))
        {

        }
    }

    public AttendeeEntry(final Integer position)
    {
        this();
        this.position = position;
        pos.setText(Integer.toString(this.position));
    }

    public AttendeeEntry()
    {
        setPreferredSize(new Dimension(900, 20));
        setMaximumSize(new Dimension(900, 20));

        type = new ButtonGroup();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {20, 131, 131, 131, 131, 131, 131, 20};
        gridBagLayout.rowHeights = new int[] {20, 0};
        gridBagLayout.columnWeights = new double[] {1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        // pos = new JLabel(position.toString());
        personNameTextField = new JTextField(10);
        personNameTextField.setPreferredSize(new Dimension(100, 20));
        personNameTextField.setMaximumSize(new Dimension(100, 20));
        personNameTextField.setHorizontalAlignment(SwingConstants.LEFT);

        GridBagConstraints personNameTextFieldLayout = new GridBagConstraints();
        personNameTextFieldLayout.fill = GridBagConstraints.BOTH;
        personNameTextFieldLayout.gridx = 1;
        personNameTextFieldLayout.gridy = 0;

        type1Day = new JRadioButton("1 Tag");
        type3Day = new JRadioButton("3 Tage");
        typeVisitor = new JRadioButton("Zuschauer");
        type.add(type1Day);
        type.add(type3Day);
        type.add(typeVisitor);

        JPanel typePanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) typePanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);

        tShirtCheckbox = new JCheckBox("T-Shirt");
        tShirtCheckbox.setHorizontalAlignment(SwingConstants.CENTER);

        paidCheckbox = new JCheckBox(new AbstractAction("Bezahlt")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                printPdfButton.setEnabled(((JCheckBox) e.getSource()).isSelected());
            }
        });

        printPdfButton = printPdfButton();
        printPdfButton.setEnabled(false);
        sentCheckbox = new JCheckBox("versendet");

        AttendeeEntry self = this;
        JButton deleteButton = new JButton(new AbstractAction("-")
        {
            private static final long serialVersionUID = -33018932692616185L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                Container parent = self.getParent();
                parent.remove(self);
//                Stream.of(parent.getComponents())
//                .filter(a -> a instanceof AttendeeEntry)
//                .map(AttendeeEntry.class::cast)
//                .sorted((a,b)->a.getpos);
                parent.revalidate();
                parent.repaint();
                EventQueue.invokeLater(() ->
                {

                });

            }
        });

        typePanel.add(type1Day);
        typePanel.add(type3Day);
        typePanel.add(typeVisitor);

        add(personNameTextField, personNameTextFieldLayout);
        final GridBagConstraints layout1 = layout(2);
        final GridBagConstraints layout2 = layout(3);
        final GridBagConstraints layout3 = layout(4);
        final GridBagConstraints layout4 = layout(5);
        final GridBagConstraints layout5 = layout(6);
        final GridBagConstraints layout6 = layout(7);
        add(pos, layout(0));
        add(typePanel, layout1);
        add(tShirtCheckbox, layout2);
        add(paidCheckbox, layout3);
        add(printPdfButton, layout4);
        add(sentCheckbox, layout5);

        add(deleteButton, layout6);
        pos.setText(position.toString());
    }

    private JButton printPdfButton()
    {
        AttendeeEntry self = this;
        return new JButton(new AbstractAction("PDF generieren")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                new SwingWorker<Object, Object>()
                {
                    @Override
                    protected Object doInBackground() throws Exception
                    {
                        final StatusBar statusBar = MainWindow.getInstance().getStatusBar();
                        statusBar.setStatus("PDF generieren");
                        statusBar.setActiveWithoutValue();
                        self.print();
                        Thread.sleep(500);
                        statusBar.reset();
                        statusBar.resetStatus();
                        return null;
                    }
                }.execute();
            }
        });
    }

    public void printOnly()
    {
        String savePath = Config.getInstance().getSavePath();
        Ticket ticket = new Ticket(getPersonName(), getType(), getTShirt());
        PDFWriter.writePdf(savePath, ticket, 2024);
    }

    public void print()
    {
        PDFWriter.preparePrint();
        printOnly();
        PDFWriter.endPrint();
    }

    private static GridBagConstraints layout(final int a)
    {
        GridBagConstraints typePanelLayout = new GridBagConstraints();
        typePanelLayout.anchor = GridBagConstraints.WEST;
        typePanelLayout.fill = GridBagConstraints.VERTICAL;
        typePanelLayout.gridx = a;
        typePanelLayout.gridy = 0;
        return typePanelLayout;
    }

    public String getPersonName()
    {
        return personNameTextField.getText();
    }

    public Integer getType()
    {
        if (type1Day.isSelected())
        {
            return 1;
        }
        else if (type3Day.isSelected())
        {
            return 2;
        }
        else if (typeVisitor.isSelected())
        {
            return 3;
        }
        else
        {
            return null;
        }

    }

    public Boolean isPaid()
    {
        return paidCheckbox.isSelected();
    }

    public Boolean isSent()
    {
        return sentCheckbox.isSelected();
    }

    public Boolean getTShirt()
    {
        return tShirtCheckbox.isSelected();
    }

    @Override
    public void fillfromCsv(final String line)
    {
        Ticket ticket = new Ticket(line);
        personNameTextField.setText(ticket.getName());
        if (ticket.getType() == 1)
        {
            type1Day.setSelected(true);
        }
        else if (ticket.getType() == 2)
        {
            type3Day.setSelected(true);
        }
        else if (ticket.getType() == 3)
        {
            typeVisitor.setSelected(true);
        }
        tShirtCheckbox.setSelected(ticket.getTShirt());
        paidCheckbox.setSelected(ticket.getPaid());
        printPdfButton.setEnabled(ticket.getPaid());
        sentCheckbox.setSelected(ticket.getSent());
    }

    @Override
    public String toCsv()
    {
        return new Ticket(
                getPersonName(),
                getType(),
                getTShirt(),
                paidCheckbox.isSelected(),
                sentCheckbox.isSelected()).toCsv();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(isPaid(), getPersonName(), pos.getText(), isSent(), getTShirt(), getType());
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        AttendeeEntry other = (AttendeeEntry) obj;
        return Objects.equals(isPaid(), other.isPaid())
                && Objects.equals(getPersonName(), other.getPersonName())
                && Objects.equals(pos.getText(), other.pos.getText())
                && Objects.equals(isSent(), other.isSent())
                && Objects.equals(getTShirt(), other.getTShirt())
                && Objects.equals(getId(), other.getId())
                && Objects.equals(getType(), other.getType());
    }

    public Integer getId()
    {
        return id;
    }

}
