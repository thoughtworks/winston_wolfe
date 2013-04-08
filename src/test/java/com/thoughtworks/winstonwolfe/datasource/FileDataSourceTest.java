package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.custommonkey.xmlunit.Diff;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

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

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getFile("key")).thenReturn(file);

        FileDataSource dataSource = new FileDataSource("key", config);
        assertThat(dataSource.getData(), is("HERE IS SOME DATA"));
    }

    @Test
    public void shouldReturnAnXmlDocument() throws Exception {
        File file = createTmpFile("<xml/>");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getFile("key")).thenReturn(file);

        FileDataSource dataSource = new FileDataSource("key", config);

        Document expected = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader("<xml/>")));
        Diff diff = new Diff(dataSource.getDocument(), expected);
        assertThat(diff.similar(), is(true));
    }

    @Test
    public void shouldNormaliseTheReturnedXmlDocument() throws Exception {
        File file = createTmpFile("<xml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><child xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></child></xml>");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getFile("key")).thenReturn(file);

        Document expected = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader("<xml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><child/></xml>")));

        FileDataSource dataSource = new FileDataSource("key", config);

        Diff diff = new Diff(dataSource.getDocument(), expected);
        assertThat(diff.similar(), is(true));
    }


    private File createTmpFile(String content) throws IOException {
        File tmp = File.createTempFile("yaml", null);
        PrintWriter writer = new PrintWriter((tmp));

        writer.print(content);
        writer.close();
        return tmp;
    }
}