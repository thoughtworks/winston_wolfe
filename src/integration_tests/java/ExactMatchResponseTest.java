import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.transform.XmlConverters.the;

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
        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/exactMatch/passingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void anExceptionIsThrownWhenResponseIsNotCorrect() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The expected response did not match the actual response.");

        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/exactMatch/failingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = getClass().getResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}