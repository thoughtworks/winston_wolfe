package integration.tests;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.config.YamlConfigLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DistributedConfigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldLoadUpConfigAndNestedFiles() throws FileNotFoundException {
        URL config = ClassLoader.getSystemResource("yaml/nested/parent.yaml");

        WinstonConfig loadedConfig = new YamlConfigLoader().load(config.getPath());

        WinstonConfig subConfig = loadedConfig.getSubConfig("sub_config_node");

        assertThat(subConfig.getString("key"), is("value"));
        assertThat(subConfig.getString("in_child1"), is("a1"));
        assertThat(subConfig.getString("in_child2"), is("a2"));
        assertThat(subConfig.getString("in_child3"), is("a2.1"));
    }

    @Test
    public void shouldFindFilesUsingCorrectRelativePaths() throws FileNotFoundException {
        URL config = ClassLoader.getSystemResource("yaml/relative_paths/relative_path.yaml");

        WinstonConfig loadedConfig = new YamlConfigLoader().load(config.getPath());

        WinstonConfig subConfig = loadedConfig.getSubConfig("sub_config_node");

        assertThat(subConfig.getString("key"), is("value"));
        assertThat(subConfig.getString("in_child"), is("a1"));
        assertThat(subConfig.getString("in_grand_child"), is("a37"));
    }
}