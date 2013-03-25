package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileDataSourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldComplainIfFileDoesNotExist() throws FileNotFoundException {

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Unable to find key file named I DO NOT EXIST");

        YamlConfig config = mock(YamlConfig.class);
        when(config.get("key")).thenReturn("I DO NOT EXIST");

        FileDataSource dataSource = new FileDataSource("key", config);
        dataSource.getData();
    }

    @Test
    public void shouldReturnDataFromFile() throws IOException {


        String fileName = createTmpFile("HERE IS SOME DATA");

        YamlConfig config = mock(YamlConfig.class);
        when(config.get("key")).thenReturn(fileName);

        FileDataSource dataSource = new FileDataSource("key", config);
        assertThat(dataSource.getData(), is("HERE IS SOME DATA"));
    }

    private String createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp.getPath();
    }

}