package com.thoughtworks.winstonwolfe.datasource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplyChangesDataSourceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldComplainIfDocumentHasNoContentMatchingASelector() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The Xpath identified as 'hobby' does not exist in the request");

        Document request = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(request);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("hobby", "//details/hobby");

        Map<String, String> changes = new HashMap<String, String>();
        changes.put("hobby", "Philately");

        ApplyChangesDataSource applyChangesDataSource = new ApplyChangesDataSource(selectors, changes, dataSource);
        applyChangesDataSource.getData();
    }

    @Test
    public void shouldComplainWhenExpectationMatcherCanNotBeFound() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Wanted to change 'sex' to 'M' but no selector called 'sex' was supplied");

        Document request = createDocument("<data><details><name>Ryan</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(request);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");

        Map<String, String> changes = new HashMap<String, String>();
        changes.put("name", "Ryan");
        changes.put("sex", "M");

        ApplyChangesDataSource applyChangesDataSource = new ApplyChangesDataSource(selectors, changes, dataSource);
        applyChangesDataSource.getData();
    }

    @Test
    public void shouldReportAValidationFailureWhenTheXpathIsNotValid() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The xpath 'name' is not valid.");

        Document request = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(request);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "/@#$^@#$");

        Map<String, String> changes = new HashMap<String, String>();
        changes.put("name", "Ryan");

        ApplyChangesDataSource applyChangesDataSource = new ApplyChangesDataSource(selectors, changes, dataSource);
        applyChangesDataSource.getData();
    }

    @Test
    public void shouldApplyChangesToData() throws Exception {
        Document request = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(request);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");

        Map<String, String> changes = new HashMap<String, String>();
        changes.put("name", "Ryan");

        String expectedResult = "<data><details><name>Ryan</name><sex>M</sex></details></data>";

        ApplyChangesDataSource applyChangesDataSource = new ApplyChangesDataSource(selectors, changes, dataSource);
        String changedData = applyChangesDataSource.getData();

        assertThat(changedData, is(expectedResult));
    }

    private Document createDocument(String xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }
}