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

public class SelectorMatchResponseTest {
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
    public void selectorMatchPasses() throws Exception {
        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/selectorMatch/responseExistsScript.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void selectorMatchIncludesSelectorsInFiles() throws Exception {
        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/selectorMatch/responseSelectorInExternalFile.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }

    @Test
    public void selectorMatchFails() throws Exception {
        expectedException.expect(RuntimeException.class);

        URL config = getClass().getResource("yaml/config.yaml");
        URL script = getClass().getResource("yaml/selectorMatch/responseDoesNotExist.yaml");

        WinstonWolfe.main(new String[]{config.getPath(), script.getPath()});
    }


    private String getResourceFileContents(String filename) throws IOException {
        URL url = getClass().getResource(filename);

        return new Scanner(new File(url.getPath())).useDelimiter("\\Z").next();
    }
}