package de.robadd.festivalmanager.updater;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DeserializerTest
{
	@Test
	public void versionTest() throws Exception
	{
		// arrange
		String json = "{\"version\":\"0.1.2-SNAPSHOT\",\"releaseNotes\":\"RELEASENOTES\",\"dependencies\":[\"foo\",\"bar\"]}";
		Update actual = Update.from(json);

		Assertions.assertThat(actual.getVersion().getMajor()).isZero();
		Assertions.assertThat(actual.getVersion().getMinor()).isEqualTo(1);
		Assertions.assertThat(actual.getVersion().getDev()).isEqualTo(2);
		Assertions.assertThat(actual.getVersion().isSnapshot()).isTrue();
		Assertions.assertThat(actual.getReleaseNotes()).isEqualTo("RELEASENOTES");
		Assertions.assertThat(actual.getDependencies()).hasSize(2);
	}
}
