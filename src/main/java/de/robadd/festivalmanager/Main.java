package de.robadd.festivalmanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.robadd.festivalmanager.ui.MainWindow;

public class Main
{
	public static void main(final String[] args) throws Exception
	{
		MainWindow.main();
	}

	private static void execute(final String[] split) throws IOException
	{
		final File backgroundImage = new File(Config.getInstance().getSavePath() + "logo-big.jpg");
		FileUtils.copyURLToFile(Main.class.getResource("/logo-big.jpg"), backgroundImage);

		for (final String string : split)
		{
			PDFWriter.writePdf(Config.getInstance().getSavePath(), string);
		}
		backgroundImage.deleteOnExit();
	}

}
