package integration.tests.http;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import infrastructure.MockSystemUnderTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class ExactMatchResponseTest {
    MockSystemUnderTest mockSUT;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
    public void noErrorIsRaisedWhenTheResponseIsCorrect() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/passingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void anExceptionIsThrownWhenResponseIsNotCorrect() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The expected response did not match the actual response.");

        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/failingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}