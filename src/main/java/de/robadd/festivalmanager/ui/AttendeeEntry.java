package de.robadd.festivalmanager.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

import de.robadd.festivalmanager.CSVWritable;
import de.robadd.festivalmanager.Config;
import de.robadd.festivalmanager.Main;
import de.robadd.festivalmanager.PDFWriter;
import de.robadd.festivalmanager.Ticket;

public final class AttendeeEntry extends JPanel implements CSVWritable
{
    private static final long serialVersionUID = -6083595259931310400L;
    private Integer position = 0;
    private JTextField personNameTextField;
    private JRadioButton type1Day;
    private JRadioButton type3Day;
    private JCheckBox tShirtCheckbox;
    private JCheckBox paidCheckbox;
    private JButton printPdfButton;
    private JCheckBox sentCheckbox;
    private JLabel pos = new JLabel();

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
        tShirtCheckbox.setSelected(ticket.getTShirt());
        paidCheckbox.setSelected(ticket.isPaid());
        printPdfButton.setEnabled(ticket.isPaid());
        sentCheckbox.setSelected(ticket.isSent());
    }

    public AttendeeEntry(final Integer position)
    {
        this();
        this.position = position;
    }

    public AttendeeEntry()
    {
        setPreferredSize(new Dimension(900, 20));
        setMaximumSize(new Dimension(900, 20));

        ButtonGroup type = new ButtonGroup();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {20, 131, 131, 131, 131, 131, 131};
        gridBagLayout.rowHeights = new int[] {20, 0};
        gridBagLayout.columnWeights = new double[] {1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
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
        type.add(type1Day);
        type.add(type3Day);

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

        typePanel.add(type1Day);
        typePanel.add(type3Day);

        add(personNameTextField, personNameTextFieldLayout);
        final GridBagConstraints layout1 = layout(2);
        final GridBagConstraints layout2 = layout(3);
        final GridBagConstraints layout3 = layout(4);
        final GridBagConstraints layout4 = layout(5);
        final GridBagConstraints layout5 = layout(6);
        add(pos, layout(0));
        add(typePanel, layout1);
        add(tShirtCheckbox, layout2);
        add(paidCheckbox, layout3);
        add(printPdfButton, layout4);
        add(sentCheckbox, layout5);
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
                EventQueue.invokeLater(self::print);
            }

        });
    }

    void print()
    {
        try
        {
            String savePath = Config.getInstance().getSavePath();
            final File backgroundImage = new File(savePath + "logo-big.jpg");
            FileUtils.copyURLToFile(Main.class.getResource("/logo-big.jpg"), backgroundImage);

            Ticket ticket = new Ticket(getPersonName(), getType(), getTShirt());
            PDFWriter.writePdf(savePath, ticket);
            Files.delete(backgroundImage.toPath());
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
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
        else
        {
            return null;
        }
    }

    public Boolean isPaid()
    {
        return paidCheckbox.isSelected();
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
        tShirtCheckbox.setSelected(ticket.getTShirt());
        paidCheckbox.setSelected(ticket.isPaid());
        printPdfButton.setEnabled(ticket.isPaid());
        sentCheckbox.setSelected(ticket.isSent());
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
}
