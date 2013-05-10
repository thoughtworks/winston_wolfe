package integration.tests.http;

import com.thoughtworks.winstonwolfe.application.WinstonWolfe;
import org.junit.*;
import org.junit.rules.ExpectedException;
import infrastructure.MockSystemUnderTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class SiblingSelectorTest {
    MockSystemUnderTest mockSUT;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void spinUpServer() throws Exception {
        mockSUT = new MockSystemUnderTest(getResourceFileContents("xml/out_with_groups.xml"));
        mockSUT.startServer();
    }

    @After
    public void stopServer() throws Exception {
        mockSUT.stopServer();
    }

    @Test
    public void selectorMatchPasses() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/siblingValidation/simple.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void nestedSelectorMatchPasses() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/siblingValidation/nested.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void nestedSelectorWithImportsMatchPasses() throws Exception {
        URL config = ClassLoader.getSystemResource("yaml/http_config.yaml");
        URL script = ClassLoader.getSystemResource("yaml/siblingValidation/nested_with_import.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    private String getResourceFileContents(String filename) throws IOException {
        URL url = ClassLoader.getSystemResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}