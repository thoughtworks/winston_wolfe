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
    public void shouldReturnDataFromFile() throws IOException {
        File file = createTmpFile("HERE IS SOME DATA");

        YamlConfig config = mock(YamlConfig.class);
        when(config.getFile("key")).thenReturn(file);

        FileDataSource dataSource = new FileDataSource("key", config);
        assertThat(dataSource.getData(), is("HERE IS SOME DATA"));
    }

    private File createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp;
    }

}