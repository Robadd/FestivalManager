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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

import de.robadd.festivalmanager.Config;
import de.robadd.festivalmanager.Main;
import de.robadd.festivalmanager.PDFWriter;
import de.robadd.festivalmanager.Ticket;

public class Entry extends JPanel
{
	private static final long serialVersionUID = -6083595259931310400L;
	private JTextField personNameTextField;
	private JRadioButton type1Day;
	private JRadioButton type3Day;
	private JCheckBox tShirtCheckbox;
	private JCheckBox paidCheckbox;
	private JButton printPdfButton;
	private JCheckBox sentCheckbox;

	public Entry(final Ticket ticket)
	{
		this();
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

	public Entry()
	{
		setPreferredSize(new Dimension(900, 20));
		setMaximumSize(new Dimension(900, 20));

		ButtonGroup type = new ButtonGroup();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 131, 131, 131, 131, 131, 131, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 20, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		personNameTextField = new JTextField(10);
		personNameTextField.setPreferredSize(new Dimension(100, 20));
		personNameTextField.setMaximumSize(new Dimension(100, 20));
		personNameTextField.setHorizontalAlignment(SwingConstants.LEFT);

		GridBagConstraints personNameTextFieldLayout = new GridBagConstraints();
		personNameTextFieldLayout.fill = GridBagConstraints.BOTH;
		personNameTextFieldLayout.gridx = 0;
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
		add(typePanel, layout(1));
		add(tShirtCheckbox, layout(2));
		add(paidCheckbox, layout(3));
		add(printPdfButton, layout(4));
		add(sentCheckbox, layout(5));
	}

	private JButton printPdfButton()
	{
		return new JButton(new AbstractAction("PDF generieren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent e)
			{
				EventQueue.invokeLater(() ->
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
				});
			}
		});
	}

	private GridBagConstraints layout(final int a)
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

	public String toCsvLine()
	{
		return new Ticket(
				getPersonName(),
				getType(),
				getTShirt(),
				paidCheckbox.isSelected(),
				sentCheckbox.isSelected()).toCsv();
	}
}
