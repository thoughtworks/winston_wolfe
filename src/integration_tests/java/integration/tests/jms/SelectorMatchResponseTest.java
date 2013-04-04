package integration.tests.jms;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import infrastructure.MockJMSBasedSystemUnderTest;
import infrastructure.MockSystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class SelectorMatchResponseTest {
    MockJMSBasedSystemUnderTest mockSUT;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void spinUpServer() throws Exception {
        mockSUT = new MockJMSBasedSystemUnderTest(getResourceFileContents("xml/out.xml"));
        mockSUT.startServer();
    }

    @After
    public void stopServer() throws Exception {
        mockSUT.stopServer();
    }

    @Test
    public void selectorMatchPasses() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/jms_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/selectorMatch/responseExistsScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void selectorMatchIncludesSelectorsInFiles() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/jms_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/selectorMatch/responseSelectorInExternalFile.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void selectorMatchFails() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The Xpath identified as 'is_available' does not exist in the response");

        URL config = ClassLoader.getSystemResource("yaml/jms_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/selectorMatch/responseDoesNotExist.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }


    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}