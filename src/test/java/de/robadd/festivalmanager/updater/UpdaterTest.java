package de.robadd.festivalmanager.updater;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class UpdaterTest
{
	private String oldV;
	private String newV;
	private boolean shouldUpdate;

	public UpdaterTest(final String oldV, final String newV, final boolean shouldUpdate)
	{
		super();
		this.oldV = oldV;
		this.newV = newV;
		this.shouldUpdate = shouldUpdate;
	}

	@Test
	public void run()
	{
		Assertions.assertThat(Updater.newerUpdate(oldV, newV)).isEqualTo(shouldUpdate);
	}

	@Parameters
	public static Collection<Object[]> data()
	{
		Collection<Object[]> retVal = new ArrayList<>();
		retVal.add(new Object[]
		{ "0.0.1", "0.0.2", true });
		retVal.add(new Object[]
		{ "0.0.1", "0.0.1", false });
		retVal.add(new Object[]
		{ "0.0.2", "0.0.1", false });
		retVal.add(new Object[]
		{ "1.0.2", "0.0.1", false });
		retVal.add(new Object[]
		{ "0.0.2", "1.0.1", true });
		retVal.add(new Object[]
		{ "1.0.2", "1.1.1", true });
		retVal.add(new Object[]
		{ "1.1.1", "1.0.2", false });
		retVal.add(new Object[]
		{ "1", "0.0.1", false });
		retVal.add(new Object[]
		{ "0.0.1", "1", true });
		retVal.add(new Object[]
		{ "0.0.1-SNAPSHOT", "1", false });
		return retVal;
	}

}
