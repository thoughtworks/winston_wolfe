package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Map;

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

        Map<String,String> expectations = new HashMap<String, String>();
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

        Map<String,String> expectations = new HashMap<String, String>();
        expectations.put("name", "Ryan");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    public void shouldComplainIfDocumentHasNoContentMatchingASelector() throws XPathExpressionException {
        String response = "<data><details><name>Perryn</name><sex>M</sex></details></data>";

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("The Xpath identified as 'hobby' does not exist in the response");

        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(response);

        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
        selectors.put("hobby", XPathFactory.newInstance().newXPath().compile("//details/hobby"));

        Map<String,String> expectations = new HashMap<String, String>();
        expectations.put("hobby", "Philately");

        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        validator.validateAgainst(dataSource);
    }

    @Test
    void shouldComplainAboutAllTheWaysADocumentDoesNotMatchExpectations() throws XPathExpressionException {
//        expectedException.expect(RuntimeException.class);
//        expectedException.expectMessage("- Expected 'Ryan' for 'name' but found 'Perryn'\n- Expected 'Ryan' for 'name' but found 'Perryn'");
//
//        String response = "<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>";
//        DataSource dataSource = mock(DataSource.class);
//        when(dataSource.getData()).thenReturn(response);
//
//        Map<String, XPathExpression> selectors = new HashMap<String, XPathExpression>();
//        selectors.put("name", XPathFactory.newInstance().newXPath().compile("//details/name"));
//
//        Map<String,String> expectations = new HashMap<String, String>();
//        expectations.put("name", "Ryan");
//        expectations.put("sex", "M");
//        expectations.put("hobby", "xpath construction");
//
//
//        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
//        validator.validateAgainst(dataSource);
    }

    //test attribute selectors?
    //should complain if a selector does not evaluate to the corresponding exepctation

    //two selectors dont match - multiple errors

    // all selectors match expectation

    // selector matches multiple nodes




//    @Test
//    public shouldCheckEachExpectationUsingTheSelectorAgainstTheDocument() {
//
//    }

    //should complain if constructuted with expectations that do not have corresponding selectors


    //should complain if no selectors are supplied

    //should complain if no expectations are supplied
}