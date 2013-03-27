package com.thoughtworks.winstonwolfe.datasource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDataSourceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getDocumentShouldComplainIfStringIsNotValidXml() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Unable to build an XML document from the supplied input");

        StringDataSource dataSource = new StringDataSource("my data");
        dataSource.getDocument();
    }

    @Test
    public void getDocumentShouldReturnADocument() throws Exception {
        StringDataSource dataSource = new StringDataSource("<xml>document</xml>");
        assertThat(dataSource.getDocument(), is(instanceOf(Document.class)));
    }
}