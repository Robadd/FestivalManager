package de.robadd.festivalmanager.model;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class BandTest
{
    @Test
    public void jsonTest()
    {
        final Band band = new Band(LocalDateTime.of(2022, 8, 6, 20, 30), LocalDateTime.of(2022, 8, 6, 21, 00), "Foobar",
                false);
        Band band2 = new Band();
        band2.fillFromJson(band.toJson());
        Assertions.assertThat(band2).isEqualTo(band);
    }
}
