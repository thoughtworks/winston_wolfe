package com.thoughtworks.winstonwolfe.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class YamlConfigLoaderIntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldThrowExceptionWhenFileNotFound() throws FileNotFoundException {
        expectedException.expect(FileNotFoundException.class);
        expectedException.expectMessage("does_not_exist.yaml");

        new YamlConfigLoader().load("does_not_exist.yaml");
    }

    @Test(expected = org.yaml.snakeyaml.parser.ParserException.class)
    public void shouldThrowExceptionWhenFileIsNotValidYaml() throws IOException {
        new YamlConfigLoader().load(createTmpFile("{this: is: invalid: yaml:}").getPath());
    }

    @Test
    public void shouldThrowExceptionWhenYamlDoesNotParseAMap() throws IOException {
        String fileName = createTmpFile("- this is not a map").getPath();
        try {
            new YamlConfigLoader().load(fileName);
        } catch (RuntimeException r) {
            assertThat(r.getMessage(), is("[" + fileName + "]" + " could not be parsed to a Map"));
        }
    }

    @Test
    public void shouldReturnConfigLoadedFromYaml() throws IOException {
        WinstonConfig config = new YamlConfigLoader().load(createTmpFile("foo: bar").getPath());
        assertThat(config.getString("foo"), is("bar"));
    }

    @Test
    public void shouldReturnPathToFile() throws IOException {
        File tmpFile = createTmpFile("file: here.txt");
        WinstonConfig config = new YamlConfigLoader().load(tmpFile.getPath());

        File expectedFile = new File(tmpFile.getParentFile().getPath() + "/" + "here.txt");

        assertThat(config.getFile("file"), is(expectedFile));
    }

    private File createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp;
    }

}