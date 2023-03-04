package de.robadd.festivalmanager.ui.button;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.robadd.festivalmanager.Main;
import de.robadd.festivalmanager.ui.entry.AttendeeEntry;
import de.robadd.festivalmanager.ui.tab.AttendeesTab;

public class AddTicketButton extends JButton
{
    private static final long serialVersionUID = -319841835226956375L;

    public AddTicketButton(final AttendeesTab parent)
    {
        super();
        setAction(new AbstractAction("+")
        {
            private static final long serialVersionUID = -7920185479985704636L;

            @Override
            public void actionPerformed(final ActionEvent e)
            {
                EventQueue.invokeLater(() ->
                {
                    final AttendeeEntry entry1 = new AttendeeEntry(parent.getAttendeeEntries().size() + 1);
                    entry1.setAlignmentY(Component.TOP_ALIGNMENT);
                    entry1.setAlignmentX(Component.LEFT_ALIGNMENT);
                    parent.getAttendeeEntries().add(entry1);
                    parent.getEntryList().add(entry1);
                    parent.getEntryList().getParent().revalidate();
                    parent.getEntryList().getParent().repaint();
                    Main.setDirty(true);
                });
            }
        });
    }
}
