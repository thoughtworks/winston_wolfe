package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
    public void shouldNotComplainIfDocumentMatchesExpectations() throws XPathExpressionException {
        String response = "<data><details><name>Perryn</name><sex>M</sex></details></data>";
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("name", XPathFactory.newInstance().newXPath().compile("//details/name"));

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Perryn");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    public void shouldComplainIfDocumentDoesNotMatchExpectations() throws XPathExpressionException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 'Ryan' for 'name' but found 'Perryn'");

        String response = "<data><details><name>Perryn</name><sex>M</sex></details></data>";
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("name", XPathFactory.newInstance().newXPath().compile("//details/name"));

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    public void shouldComplainIfDocumentHasNoContentMatchingASelector() throws XPathExpressionException {
        String response = "<data><details><name>Perryn</name><sex>M</sex></details></data>";
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("hobby", XPathFactory.newInstance().newXPath().compile("//details/hobby"));

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
    public void shouldComplainAboutAllTheWaysADocumentDoesNotMatchExpectations() throws XPathExpressionException {
        String response = "<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>";
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("name", XPathFactory.newInstance().newXPath().compile("//details/name"));
        selectors.put("sex", XPathFactory.newInstance().newXPath().compile("/data/details/sex"));
        selectors.put("hobby", XPathFactory.newInstance().newXPath().compile("//details/hobby"));
        selectors.put("thursday", XPathFactory.newInstance().newXPath().compile("//details/towelday"));

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
    public void shouldComplainWhenExpectationMatcherCanNotBeFound() throws XPathExpressionException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 'sex' to be 'M' but no selector called 'sex' was supplied");

        String response = "<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>";
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("name", XPathFactory.newInstance().newXPath().compile("//details/name"));

        Map<String, String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");
        expectations.put("sex", "M");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }
}