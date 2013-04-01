package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiFileConfigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldDoNothingAdditionalIfThereIsNoImportFilesKeyAtTheRootNode() throws FileNotFoundException {
        ConfigLoader loader = mock(ConfigLoader.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        assertThat(config.getString("key"), is("value"));
    }

    @Test
    public void shouldMergeConfigFromFiles() throws FileNotFoundException {
        List<String> file_names = new ArrayList<String>();
        file_names.add("path");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("import_files", file_names);

        Map<String, Object> fileConfigMap = new HashMap<String, Object>();
        fileConfigMap.put("key", "value");

        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(subConfig.getMap()).thenReturn(fileConfigMap);

        ConfigLoader loader = mock(ConfigLoader.class);
        when(loader.load("basePath/path")).thenReturn(subConfig);

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        assertThat(config.getString("key"), is("value"));
        assertThat(config.getMap().get("import_files"), is(nullValue()));
    }


    @Test
    public void shouldMergeSubConfigFromFiles() throws FileNotFoundException {
        List<String> file_names = new ArrayList<String>();
        file_names.add("path");

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("foo", subMap);
        subMap.put("import_files", file_names);

        Map<String, Object> fileConfigMap = new HashMap<String, Object>();
        fileConfigMap.put("key", "value");

        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(subConfig.getMap()).thenReturn(fileConfigMap);

        ConfigLoader loader = mock(ConfigLoader.class);
        when(loader.load("basePath/path")).thenReturn(subConfig);

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        assertThat(config.getSubConfig("foo").getString("key"), is("value"));
        assertThat(config.getSubConfig("foo").getMap().get("import_files"), is(nullValue()));
    }

    @Test
    public void shouldComplainIfSubConfigFileCantBeFound() throws FileNotFoundException {
        expectedException.expect(RuntimeException.class);

        List<String> file_names = new ArrayList<String>();
        file_names.add("path");

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("foo", subMap);
        subMap.put("import_files", file_names);

        ConfigLoader loader = mock(ConfigLoader.class);
        when(loader.load("basePath/path")).thenThrow(new FileNotFoundException());

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        config.getSubConfig("foo");
    }

    @Test
    public void shouldThrowABetterErrorWhenAFileCantBeFound() throws FileNotFoundException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Couldn't find file 'basePath/path/to/non-existent/file (No such file or directory)' referenced by configuration key 'response_selectors'");

        List<String> file_names = new ArrayList<String>();
        file_names.add("path/to/non-existent/file");

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        map.put("response_selectors", subMap);
        subMap.put("import_files", file_names);

        ConfigLoader loader = new YamlConfigLoader();

        MultiFileConfig config = new MultiFileConfig(map, "basePath", loader);
        config.getSubConfig("response_selectors");
    }
}