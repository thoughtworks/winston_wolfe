package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiFileConfigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldDoNothingAdditionalIfThereIsNoImportFilesKeyAtTheRootNode() {
        ConfigLoader loader = mock(ConfigLoader.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        assertThat(config.getString("key"), is("value"));
    }

    @Test
    public void shouldMergeConfigFromFile() {
        ConfigLoader loader = mock(ConfigLoader.class);
        WinstonConfig subConfig = mock(WinstonConfig.class);

        List<String> file_names = new ArrayList<String>();
        file_names.add("path");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("import_files", file_names);

        when(loader.load("basePath/path")).thenReturn(subConfig);

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        assertThat(config.getString("key"), is("value"));
    }


    @Test
    public void shouldFindFilesUsingCorrectRelativePaths() {
        fail("aww");
    }
}