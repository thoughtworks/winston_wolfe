package integration.tests;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testInfrastructure.MockSystemUnderTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class HtmlReportTest {
    MockSystemUnderTest mockSUT;

    @Before
    public void spinUpServer() throws Exception {
        mockSUT = new MockSystemUnderTest(getResourceFileContents("xml/out.xml"));
        mockSUT.startServer();
    }

    @After
    public void stopServer() throws Exception {
        mockSUT.stopServer();
    }

    @Test
    public void shouldGenerateAnHtmlReportOnSuccess() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/passingTestScript.yaml");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});

        assertThat(baos.toString(), containsString("<html>"));
        assertThat(baos.toString(), containsString("<div id=\"request\">"));
        assertThat(baos.toString(), containsString("<div id=\"response\">"));
        assertThat(baos.toString(), containsString("<div id=\"satisfactions\">"));
        assertThat(baos.toString(), containsString("<div id=\"disappointments\">"));
        assertThat(baos.toString(), containsString("</html>"));
    }

    @Test
    public void shouldGenerateAnHtmlReportOnFailure() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/failingTestScript.yaml");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try {
            WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
        } catch (RuntimeException e) {
            assertThat(baos.toString(), containsString("<html>"));
            assertThat(baos.toString(), containsString("<div id=\"request\">"));
            assertThat(baos.toString(), containsString("<div id=\"response\">"));
            assertThat(baos.toString(), containsString("<div id=\"satisfactions\">"));
            assertThat(baos.toString(), containsString("<div id=\"disappointments\">"));
            assertThat(baos.toString(), containsString("</html>"));
        }
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}