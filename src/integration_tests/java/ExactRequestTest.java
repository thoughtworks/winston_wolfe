import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExactRequestTest {
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
    public void theInputXmlWillBeSentToTheEndpoint() throws Exception {
        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/exactMatch/passingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});

        assertThat(mockSUT.getLastRequest(), is(getResourceFileContents("xml/in.xml")));
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = getClass().getResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}