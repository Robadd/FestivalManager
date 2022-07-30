package de.robadd.festivalmanager.updater;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UpdaterTest
{
    @Test(dataProvider = "data")
    public void run(final String oldV, final String newV, final boolean shouldUpdate)
    {
        Assertions.assertThat(Updater.newerUpdate(oldV, newV)).isEqualTo(shouldUpdate);
    }

    @DataProvider(name = "data")
    public static Object[][] data()
    {

        return new Object[][] {
                {"0.0.1", "0.0.2", true},
                {"0.0.1", "0.0.1", false},
                {"0.0.2", "0.0.1", false},
                {"1.0.2", "0.0.1", false},
                {"0.0.2", "1.0.1", true},
                {"1.0.2", "1.1.1", true},
                {"1.1.1", "1.0.2", false},
                {"1", "0.0.1", false},
                {"0.0.1", "1", true},
                {"0.0.1-SNAPSHOT", "1", false},
        };
    }

}
