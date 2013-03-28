package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class YamlConfigTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldThrowExceptionWhenFileNotFound() throws FileNotFoundException {
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage("The file 'does_not_exist.yaml' could not be found");

        new YamlConfig("does_not_exist.yaml");
    }

    @Test(expected = org.yaml.snakeyaml.parser.ParserException.class)
    public void shouldThrowExceptionWhenFileIsNotValidYaml() throws IOException {
        new YamlConfig(createTmpFile("{this: is: invalid: yaml:}").getPath());
    }

    @Test
    public void shouldThrowExceptionWhenYamlDoesNotParseAMap() throws IOException {
        String fileName = createTmpFile("- this is not a map").getPath();
        try {
            new YamlConfig(fileName);
        } catch (RuntimeException r) {
            assertThat(r.getMessage(), is("[" + fileName + "]" + " could not be parsed to a Map"));
        }
    }

    @Test
    public void shouldAllowLookupOfKey() throws IOException {
        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo: bar").getPath());
        assertThat(yamlConfig.get("foo"), is("bar"));
    }

    @Test
    public void shouldComplainIfTryToLookupMissingKey() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("No missing specified in yaml");
        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo: bar").getPath());
        assertThat(yamlConfig.get("missing"), is("bar"));
    }

    @Test
    public void shouldGetTheLocationOfTheFileAtKeyWithoutSlash() throws IOException {
        File tmpFile = createTmpFile("file: here.txt");
        YamlConfig yamlConfig = new YamlConfig(tmpFile.getPath());

        File expectedFile = new File(tmpFile.getParentFile().getPath() + "/" + "here.txt");

        assertThat(yamlConfig.getFile("file"), is(expectedFile));
    }

    @Test
    public void shouldGetTheLocationOfTheFileAtKeyWithSlash() throws IOException {
        File tmpFile = createTmpFile("file: /here.txt");
        YamlConfig yamlConfig = new YamlConfig(tmpFile.getPath());

        File expectedFile = new File(tmpFile.getParentFile().getPath() + "/" + "here.txt");

        assertThat(yamlConfig.getFile("file"), is(expectedFile));
    }

    @Test
    public void shouldReturnValueIfKeyExists() throws IOException {
        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo: bar").getPath());
        assertThat(yamlConfig.exists("foo"), is(true));
        assertThat(yamlConfig.exists("herp"), is(false));
    }






    @Test
    public void getKeyValueMapShouldComplainIfTryToLookupMissingKey() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Element 'foo' could not be found in the yaml");

        YamlConfig yamlConfig = new YamlConfig(createTmpFile("derp: bar").getPath());
        yamlConfig.getKeyValueMap("foo");
    }

    @Test
    public void getKeyValueMapShouldComplainWhenKeyDoesNoReferToAMap() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Element 'foo' is not a map.");

        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo: bar").getPath());
        yamlConfig.getKeyValueMap("foo");
    }

    @Test
    public void getKeyValueMapShouldReturnAKeyValueMap() throws IOException {
        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo:\n  child: bar").getPath());

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("child", "bar");

        assertThat(yamlConfig.getKeyValueMap("foo"), is(expected));
    }

    @Test
    public void getKeyValueMapShouldComplainIfImportFilesIsNotAList() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Element 'import_files' is not a list.");

        File tmpFile = createTmpFile("imported: value");

        String yaml = String.format("parent:\n  import_files: %s", tmpFile.getPath());
        YamlConfig yamlConfig = new YamlConfig(createTmpFile(yaml).getPath());
        yamlConfig.getKeyValueMap("parent");
    }

    @Test
    public void getKeyValueMapShouldComplainIfImportFilesDoesNotExist() throws IOException {

    }

    @Test
    public void getKeyValueMapShouldImportFiles() throws IOException {
        File tmpFile = createTmpFile("imported: value");

        String yaml = String.format("parent:\n  import_files:\n    - %s", tmpFile.getPath());
        YamlConfig yamlConfig = new YamlConfig(createTmpFile(yaml).getPath());

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("imported", "value");

        assertThat(yamlConfig.getKeyValueMap("parent"), is(expected));
    }





    @Test
    public void getListShouldComplainIfItIsNotAList() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Element 'parent' is not a list.");

        YamlConfig yamlConfig = new YamlConfig(createTmpFile("parent: value").getPath());
        yamlConfig.getList("parent");
    }

    @Test
    public void getListShouldComplainIfKeyDoesNotExist() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Element 'foo' could not be found in the yaml");

        YamlConfig yamlConfig = new YamlConfig(createTmpFile("derp: bar").getPath());
        yamlConfig.getList("foo");
    }

    @Test
    public void getListShouldReturnAList() throws IOException {
        List<String> expected = new ArrayList<String>();
        expected.add("bar");

        YamlConfig yamlConfig = new YamlConfig(createTmpFile("derp:\n  - bar").getPath());
        assertThat(yamlConfig.getList("derp"), is(expected));
    }


    private File createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp;
    }
}