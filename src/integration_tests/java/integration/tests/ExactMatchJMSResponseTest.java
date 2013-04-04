package integration.tests;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import infrastructure.MockJMSBasedSystemUnderTest;
import infrastructure.MockSystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.jms.JMSException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ExactMatchJMSResponseTest {

    MockJMSBasedSystemUnderTest mockSUT;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void spinUpServer() throws Exception {
        mockSUT = new MockJMSBasedSystemUnderTest(getResourceFileContents("xml/out.xml"));
        mockSUT.startServer();
    }

    @After
    public void shutDownServer() throws JMSException {
        mockSUT.stopServer();
    }

    @Test
    public void noErrorIsRaisedWhenTheResponseIsCorrect() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/jms_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/passingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void anExceptionIsThrownWhenResponseIsNotCorrect() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The expected response did not match the actual response.");

        URL config = ClassLoader.getSystemResource("yaml/jms_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/failingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }

}