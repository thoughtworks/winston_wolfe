package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
    public void shouldReturnTryIfKeyExists() throws IOException {
        YamlConfig yamlConfig = new YamlConfig(createTmpFile("foo: bar").getPath());
        assertThat(yamlConfig.exists("foo"), is(true));
        assertThat(yamlConfig.exists("herp"), is(false));
    }

    private File createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp;
    }
}