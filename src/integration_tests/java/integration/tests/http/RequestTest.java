package integration.tests.http;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import com.thoughtworks.winstonwolfe.datasource.ApplyChangesDataSource;
import com.thoughtworks.winstonwolfe.datasource.StringDataSource;
import infrastructure.MockSystemUnderTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestTest {
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
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/exactMatch/passingTestScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});

        assertThat(mockSUT.getLastRequest(), is(getResourceFileContents("xml/in.xml")));
    }

    @Test
    public void theInputXmlCanBeModifiedBeforeBeingSent() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/overrideRequest/override.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});

        ApplyChangesDataSource expected = new ApplyChangesDataSource(new HashMap<String, String>(), new HashMap<String, String>(), new StringDataSource(getResourceFileContents("xml/changed_in_message.xml")));

        assertThat(mockSUT.getLastRequest(), is(expected.getData()));
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}