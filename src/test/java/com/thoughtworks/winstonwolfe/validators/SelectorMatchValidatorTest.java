package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectorMatchValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotComplainIfDocumentMatchesExpectations() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Perryn");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    private Document createDocument(String xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    @Test
    public void shouldComplainIfDocumentDoesNotMatchExpectations() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 'Ryan' for 'name' but found 'Perryn'");

        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    public void shouldComplainIfDocumentHasNoContentMatchingASelector() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("hobby", "//details/hobby");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("hobby", "Philately");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        try {
            validator.validateAgainst(dataSource);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("The Xpath identified as 'hobby' does not exist in the response"));
            assertThat(e.getMessage(), not(containsString("Expected 'Philately' for 'hobby' but found ''")));
        }

    }

    @Test
    public void shouldComplainAboutAllTheWaysADocumentDoesNotMatchExpectations() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");
        selectors.put("sex", "/data/details/sex");
        selectors.put("hobby", "//details/hobby");
        selectors.put("thursday", "//details/towelday");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");
        expectations.put("sex", "M");
        expectations.put("hobby", "xpath construction");
        expectations.put("thursday", "towel");


        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        try {
            validator.validateAgainst(dataSource);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("Expected 'Ryan' for 'name' but found 'Perryn'"));
            assertThat(e.getMessage(), containsString("Expected 'xpath construction' for 'hobby' but found 'philately'"));
            assertThat(e.getMessage(), containsString("The Xpath identified as 'thursday' does not exist in the response"));
        }
    }

    @Test
    public void shouldComplainWhenExpectationMatcherCanNotBeFound() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 'sex' to be 'M' but no selector called 'sex' was supplied");

        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "//details/name");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");
        expectations.put("sex", "M");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    public void shouldReportAValidationFailureWhenTheXpathIsNotValid() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectors = new HashMap<String, String>();
        selectors.put("name", "/@#$^@#$");

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        try {
            validator.validateAgainst(dataSource);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString("The xpath 'name' is not valid. Refer to the horrible stack trace on the console."));
        }
    }
}