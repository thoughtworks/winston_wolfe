import com.thoughtworks.winstonwolfe.runner.WinstonWolfe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.transform.XmlConverters.the;

public class RequestResponseTest {
    MockSystemUnderTest mockSUT;

    @Before
    public void spinUpServer() throws Exception {
        mockSUT = new MockSystemUnderTest("<h1>Dood! I ARE NOT VALID</h1>");
        mockSUT.startServer();
    }

    @After
    public void stopServer() throws Exception {
        mockSUT.stopServer();
    }

    public String getResourceFileContents(String filename) throws IOException {
        URL url = getClass().getResource(filename);

        InputStream fis;
        BufferedReader br;
        String line;
        StringBuilder fileContents = new StringBuilder();

        fis = new FileInputStream(url.getPath());
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        while ((line = br.readLine()) != null) {
            fileContents.append(line);
        }

        return fileContents.toString();
    }

    @Test
    public void theInputXmlWillBeSentToTheEndpoint() throws Exception {
        String resourcesPath = "src/integration_tests/resources/";
        WinstonWolfe.main(new String[]{resourcesPath + "yaml/config.yaml", resourcesPath + "yaml/testScript.yaml"});

        assertThat(mockSUT.getLastRequest(), is(getResourceFileContents("xml/in.xml")));
    }

    @Test(expected = Exception.class)
    public void anExceptionIsThrownWhenResponseIsNotCorrect() throws Exception {
//        WinstonWolfe.main(new String[]{"./yaml/config.yaml", "./yaml/testScript.yaml"});
    }
}