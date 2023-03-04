package de.robadd.festivalmanager.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Updater
{

    private Updater()
    {
    }

    public static void updateSoftware(final String currVersion)
    {
        Optional<Version> newestUpdate = listUpdates().stream().map(Update::getVersion).sorted((a, b) -> b.compareTo(a))
                .findFirst();
        if (newestUpdate.isPresent() && newestUpdate.get().compareTo(new Version(currVersion)) > 0)
        {
            download(newestUpdate.get());
        }
    }

    private static void download(final Version version)
    {
        // Empty for now
    }

    public static boolean newerUpdate(final String oldVersion, final String newVersion)
    {
        Version oldV = new Version(oldVersion);
        Version newV = new Version(newVersion);
        return oldV.compareTo(newV) < 0;
    }

    public static String getReleaseNotes(final String currentVersion)
    {
        Version currVersion = new Version(currentVersion);

        return listUpdates().stream().filter(a -> a.getVersion().compareTo(currVersion) > 0).map(a ->
        {
            StringBuilder notes = new StringBuilder();
            notes.append(a.getVersion())
                    .append("\n--------------\n")
                    .append(a.getReleaseNotes())
                    .append('\n');
            return notes.toString();
        }).collect(Collectors.joining("\n\n"));

    }

    public static List<Update> listUpdates()
    {
        try
        {
            URL url = new URL("https://www.reiserdorfer-haisl.de/festivalmanager/versions.php");
            InputStream inputStream = url.openConnection().getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            for (String line; (line = reader.readLine()) != null;)
            {
                builder.append(line);
            }
            return Update.fromList(builder.toString());
        }
        catch (IOException e)
        {
            return Collections.emptyList();
        }

    }

    public static String isUpdateAvailable(final String string)
    {
        Optional<Version> newestUpdate = listUpdates().stream().map(Update::getVersion).sorted((a, b) -> b.compareTo(a))
                .findFirst();
        if (newestUpdate.isPresent() && newestUpdate.get().compareTo(new Version(string)) > 0)
        {
            return newestUpdate.get().toString();
        }
        return null;
    }
}
