package de.robadd.festivalmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.UnsupportedLookAndFeelException;

import de.robadd.festivalmanager.ui.MainWindow;
import de.robadd.festivalmanager.ui.UpdaterWindow;
import de.robadd.festivalmanager.updater.Updater;

public class Main
{
	public static void main(final String[] args) throws Exception
	{
		MainWindow.main();
		showUpdateWindowIfNecessary();
	}

	private static void showUpdateWindowIfNecessary() throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException
	{
		InputStream inputStream = Main.class.getResourceAsStream("/version.properties");
		if (inputStream != null)
		{
			System.out.println("inputStream found");
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			for (String line; (line = reader.readLine()) != null;)
			{
				String[] split = line.split("=");
				System.out.println("readLine: '" + split[0] + "'");
				if ("version".equals(split[0]))
				{
					String version = Updater.isUpdateAvailable(split[1]);
					if (version != null)
					{
						System.out.println("version found");
						UpdaterWindow.main(version);
					}
				}
			}
		}
	}
}
