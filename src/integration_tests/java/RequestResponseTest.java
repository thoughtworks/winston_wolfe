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

public class RequestResponseTest {
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
    public void theInputXmlWillBeSentToTheEndpoint() throws Exception {
        String resourcesPath = "src/integration_tests/resources/";
        WinstonWolfe.main(new String[]{resourcesPath + "yaml/config.yaml", resourcesPath + "yaml/passingTestScript.yaml"});

        assertThat(mockSUT.getLastRequest(), is(getResourceFileContents("xml/in.xml")));
    }

    @Test
    public void noErrorIsRaisedWhenTheResponseIsCorrect() throws Exception {
        String resourcesPath = "src/integration_tests/resources/";
        WinstonWolfe.main(new String[]{resourcesPath + "yaml/config.yaml", resourcesPath + "yaml/passingTestScript.yaml"});
    }

    @Test
    public void anExceptionIsThrownWhenResponseIsNotCorrect() throws Exception {
        expectedException.expect(RuntimeException.class);

        String resourcesPath = "src/integration_tests/resources/";
        WinstonWolfe.main(new String[]{resourcesPath + "yaml/config.yaml", resourcesPath + "yaml/failingTestScript.yaml"});
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = getClass().getResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}