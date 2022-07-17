package de.robadd.festivalmanager.ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

public class StatisticsTab extends JPanel implements TabbedOnChangeListener
{
	private static final Logger LOG = LoggerFactory.getLogger(StatisticsTab.class);
	private static final long serialVersionUID = 1L;
	private StatisticsEntry countEntry;
	private StatisticsEntry countType1Entry;
	private StatisticsEntry countType2Entry;
	private StatisticsEntry paidEntry;
	private StatisticsEntry tshirtEntry;
	private int count;
	private int paid;
	private int tshirt;
	private int countType1;
	private int countType2;

	public StatisticsTab()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		countEntry = new StatisticsEntry("Teilnehmer", null, null);
		add(countEntry);

		countType1Entry = new StatisticsEntry("Tages-Tickets", null, null);
		add(countType1Entry);

		countType2Entry = new StatisticsEntry("3-Tages-Tickets", null, null);
		add(countType2Entry);

		paidEntry = new StatisticsEntry("Bezahlt", null, null);
		add(paidEntry);

		tshirtEntry = new StatisticsEntry("T-Shirts", null, null);
		add(tshirtEntry);

	}

	@Override
	public void focusChanged(final MainWindow window)
	{
		count = 0;
		paid = 0;
		tshirt = 0;
		countType1 = 0;
		countType2 = 0;
		window.getEntries().forEach(entry ->
		{
			if (StringUtils.isEmptyOrWhitespace(entry.getPersonName()))
			{
				return;
			}
			count++;
			if (entry.getTShirt())
			{
				tshirt++;
			}
			if (entry.isPaid())
			{
				paid++;
			}
			if (entry.getType() == 1)
			{
				countType1++;
			}
			if (entry.getType() == 2)
			{
				countType2++;
			}
		});
		setValues();
		revalidate();
		repaint();
	}

	private void setValues()
	{
		countEntry.setValue(count);

		countType1Entry.setValue(countType1);
		countType1Entry.setPercentage((float) countType1 / count);

		countType2Entry.setValue(countType2);
		countType2Entry.setPercentage((float) countType2 / count);

		paidEntry.setValue(paid);
		paidEntry.setPercentage((float) paid / count);

		tshirtEntry.setValue(tshirt);
		tshirtEntry.setPercentage((float) tshirt / count);

	}

}
