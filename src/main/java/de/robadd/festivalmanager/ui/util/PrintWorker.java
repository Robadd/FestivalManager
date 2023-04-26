package de.robadd.festivalmanager.ui.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import de.robadd.festivalmanager.ui.MainWindow;
import de.robadd.festivalmanager.ui.StatusBar;
import de.robadd.festivalmanager.ui.entry.AttendeeEntry;
import de.robadd.festivalmanager.util.PDFWriter;

public final class PrintWorker extends SwingWorker<Object, Object>
{

    private List<AttendeeEntry> paidTickets;
    private StatusBar statusBar;
    private AtomicInteger current = new AtomicInteger(0);

    public PrintWorker(final List<AttendeeEntry> tickets)
    {
        super();
        this.paidTickets = tickets.stream()
                .filter(AttendeeEntry::isPaid)
                .collect(Collectors.toList());

        statusBar = MainWindow.getInstance().getStatusBar();

        final int entrySize = paidTickets.size();
        statusBar.setMax(entrySize);
        statusBar.setStatus("Vorbereitung");
    }

    private void increment()
    {
        synchronized (current)
        {
            statusBar.increment();
            statusBar.setStatus("PDF generieren (" + current.incrementAndGet() + " / " + paidTickets.size() + ")");
        }
    }

    @Override
    protected Object doInBackground() throws Exception
    {
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(paidTickets.size());

        PDFWriter.preparePrint();
        paidTickets.parallelStream()
                .map(a -> CompletableFuture.supplyAsync(() -> a, newFixedThreadPool))
                .map(a -> a.thenAcceptAsync(AttendeeEntry::printOnly, newFixedThreadPool))
                .map(a -> a.thenRunAsync(this::increment, newFixedThreadPool))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        PDFWriter.endPrint();
        newFixedThreadPool.shutdown();
        return null;
    }

    @Override
    protected void done()
    {
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        statusBar.reset();
        statusBar.resetStatus();
    }

}
