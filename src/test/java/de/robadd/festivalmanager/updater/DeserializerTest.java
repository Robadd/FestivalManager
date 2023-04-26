package de.robadd.festivalmanager.updater;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class DeserializerTest
{
    @Test
    public void versionTest() throws Exception
    {
        // arrange
        String json = "{\"version\":\"0.1.2-SNAPSHOT\",\"releaseNotes\":[\"RELEASENOTES\"],\"dependencies\":[\"foo\",\"bar\"]}";
        Update actual = Update.from(json);

        Assertions.assertThat(actual.getVersion().getMajor()).isZero();
        Assertions.assertThat(actual.getVersion().getMinor()).isEqualTo(1);
        Assertions.assertThat(actual.getVersion().getDev()).isEqualTo(2);
        Assertions.assertThat(actual.getVersion().isSnapshot()).isTrue();
        Assertions.assertThat(actual.getReleaseNotes()).isNotEmpty().singleElement().isEqualTo("RELEASENOTES");
        Assertions.assertThat(actual.getDependencies()).hasSize(2);
    }

    @Test
    public void versionsTest() throws Exception
    {
        // arrange
        String json = "[{\"version\":\"0.3.2\",\"releaseNotes\":[\"Added foo\"]},"
                + "{\"version\":\"0.1.1\",\"dependencies\":[],\"releaseNotes\":[\"Added bla\"]},"
                + "{\"version\":\"0.1.2\",\"dependencies\":[],\"releaseNotes\":[\"Added foo\"]}]";
        List<Update> actual = Update.fromList(json);

        Assertions.assertThat(actual).isNotEmpty().hasSize(3);
    }

    @Test
    public void dependenciesTest() throws Exception
    {
        String json = "{\"version\":\"0.1.2\",\"dependencies\":[ \r\n"
                + "    \"javase-3.5.0.jar\",\r\n"
                + "    \"core-3.5.0.jar\",\r\n"
                + "    \"thymeleaf-3.1.0.M2.jar\",\r\n"
                + "    \"flying-saucer-pdf-9.1.22.jar\",\r\n"
                + "    \"commons-io-2.11.0.jar\",\r\n"
                + "    \"slf4j-simple-2.0.6.jar\",\r\n"
                + "    \"jackson-annotations-2.14.2.jar\",\r\n"
                + "    \"jackson-core-2.14.2.jar\",\r\n"
                + "    \"jackson-datatype-jsr310-2.14.2.jar\",\r\n"
                + "    \"jackson-databind-2.14.2.jar\",\r\n"
                + "    \"mariadb-java-client-2.1.2.jar\",\r\n"
                + "    \"jcommander-1.82.jar\",\r\n"
                + "    \"jai-imageio-core-1.4.0.jar\",\r\n"
                + "    \"ognl-3.2.21.jar\",\r\n"
                + "    \"attoparser-2.0.5.RELEASE.jar\",\r\n"
                + "    \"unbescape-1.1.6.RELEASE.jar\",\r\n"
                + "    \"slf4j-api-2.0.6.jar\",\r\n"
                + "    \"itext-2.1.7.jar\",\r\n"
                + "    \"bctsp-jdk14-1.46.jar\",\r\n"
                + "    \"bcmail-jdk14-1.64.jar\",\r\n"
                + "    \"bcpkix-jdk14-1.64.jar\",\r\n"
                + "    \"bcprov-jdk14-1.64.jar\",\r\n"
                + "    \"flying-saucer-core-9.1.22.jar\",\r\n"
                + "    \"javassist-3.24.1-GA.jar\"\r\n"
                + "],\"releaseNotes\":[\"Added foo\"]}";
        Update actual = Update.from(json);
        String json2 = "{\"version\":\"0.1.2\",\"dependencies\":[ \r\n"
                + "    \"javase-3.5.1.jar\",\r\n"
                + "    \"core-3.5.1.jar\",\r\n"
                + "    \"thymeleaf-3.1.1.RELEASE.jar\",\r\n"
                + "    \"flying-saucer-pdf-9.1.22.jar\",\r\n"
                + "    \"commons-io-2.11.0.jar\",\r\n"
                + "    \"slf4j-simple-2.0.6.jar\",\r\n"
                + "    \"jackson-annotations-2.14.2.jar\",\r\n"
                + "    \"jackson-core-2.14.2.jar\",\r\n"
                + "    \"jackson-datatype-jsr310-2.14.2.jar\",\r\n"
                + "    \"jackson-databind-2.14.2.jar\",\r\n"
                + "    \"mariadb-java-client-3.1.2.jar\",\r\n"
                + "    \"jcommander-1.82.jar\",\r\n"
                + "    \"jai-imageio-core-1.4.0.jar\",\r\n"
                + "    \"ognl-3.3.4.jar\",\r\n"
                + "    \"attoparser-2.0.6.RELEASE.jar\",\r\n"
                + "    \"unbescape-1.1.6.RELEASE.jar\",\r\n"
                + "    \"waffle-jna-3.2.0.jar\",\r\n"
                + "    \"jcl-over-slf4j-1.7.36.jar\",\r\n"
                + "    \"slf4j-api-2.0.6.jar\",\r\n"
                + "    \"itext-2.1.7.jar\",\r\n"
                + "    \"bctsp-jdk14-1.46.jar\",\r\n"
                + "    \"bcmail-jdk14-1.64.jar\",\r\n"
                + "    \"bcpkix-jdk14-1.64.jar\",\r\n"
                + "    \"bcprov-jdk14-1.64.jar\",\r\n"
                + "    \"flying-saucer-core-9.1.22.jar\",\r\n"
                + "    \"javassist-3.29.0-GA.jar\",\r\n"
                + "    \"jna-platform-5.12.1.jar\",\r\n"
                + "    \"jna-5.12.1.jar\",\r\n"
                + "    \"caffeine-2.9.3.jar\",\r\n"
                + "    \"checker-qual-3.23.0.jar\",\r\n"
                + "    \"error_prone_annotations-2.10.0.jar\"\r\n"
                + "],\"releaseNotes\":[\"Added foo\"]}";
        Update actual2 = Update.from(json2);
        List<String> oldDep = actual.getDependencies();
        List<String> newDep = actual2.getDependencies();
        List<String> toAdd = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();
        toAdd.addAll(newDep);
        toAdd.removeAll(oldDep);
        toRemove.addAll(oldDep);
        toRemove.removeAll(newDep);
        Assertions.assertThat(toRemove).noneSatisfy(a -> toAdd.contains(a));
        System.out.println("To add:\n" + toAdd);
        System.out.println("To remove:\n" + toRemove);
    }
}
