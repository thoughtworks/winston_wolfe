package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Ignore;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectorMatchValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnValidationResultWithSuccessfulMessageIfDocumentMatchesExpectations() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);


        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "//details/name");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);


        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Perryn");

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));


        ValidationResults results = runValidationTest(dataSource, selectors, expectations);


        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'Perryn' for 'name'");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnValidationResultWhenMatchingOnAttributes() throws Exception {
        Document response = createDocument("<data><details name=\"Perryn\"><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        WinstonConfig selectors = mock(WinstonConfig.class);
        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "//details/@name");
        when(selectors.getFlatStringMap()).thenReturn(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Perryn");

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'Perryn' for 'name'");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnValidationResultWhenMatchingWithSiblings() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details><details><name>Jacqui</name><sex>F</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("sex", "//details/sex[../name/text() = \"Jacqui\"]");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("sex", "F");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'F' for 'sex'");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnValidationResultWhenMatchingWithMultipleSiblings() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><age>37</age></details><details><name>Jacqui</name><sex>F</sex><age>36</age></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("sex", "//details/sex[../name/text() = \"Jacqui\"][../age/text() = \"36\"]");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("sex", "F");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'F' for 'sex'");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldComplainIfDocumentDoesNotMatchExpectations() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "//details/name");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Ryan");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("Expected 'Ryan' for 'name' but found 'Perryn'");
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldComplainIfDocumentHasNoContentMatchingASelector() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("hobby", "//details/hobby");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("hobby", "Philately");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));


        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("The Xpath identified as 'hobby' does not exist in the response");
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldComplainAboutAllTheWaysADocumentDoesNotMatchExpectations() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "//details/name");
        selectorMap.put("sex", "/data/details/sex");
        selectorMap.put("hobby", "//details/hobby");
        selectorMap.put("thursday", "//details/towelday");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Ryan");
        expectationsMap.put("sex", "M");
        expectationsMap.put("hobby", "xpath construction");
        expectationsMap.put("thursday", "towel");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("The Xpath identified as 'thursday' does not exist in the response");
        failureMessages.add("Expected 'Ryan' for 'name' but found 'Perryn'");
        failureMessages.add("Expected 'xpath construction' for 'hobby' but found 'philately'");
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldComplainWhenExpectationMatcherCanNotBeFound() throws Exception {
        Document response = createDocument("<data><details><name>Ryan</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "//details/name");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Ryan");
        expectationsMap.put("sex", "M");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);
        when(expectations.getFlatStringMap()).thenReturn(buildFlatStringMap(expectationsMap));

        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("Expected 'sex' to be 'M' but no selector called 'sex' was supplied");
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldReportAValidationFailureWhenTheXpathIsNotValid() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex><hobby>philately</hobby></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> selectorMap = new HashMap<String, String>();
        selectorMap.put("name", "/@#$^@#$");
        WinstonConfig selectors = newMockWinstonConfigWithFlatStringMap(selectorMap);

        Map<String, Object> expectationsMap = new HashMap<String, Object>();
        expectationsMap.put("name", "Ryan");
        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationsMap);


        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("The xpath 'name' is not valid. Refer to the horrible stack trace on the console.");
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldComplainIfASiblingDoesNotContainARoot() throws ParserConfigurationException, SAXException, IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Sibling validation 'details' does not contain an element called 'root'");

        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details><details><name>Perryn</name><sex>F</sex></details><details><name>Ryan</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> detailSiblingMap = new HashMap<String, String>();
        WinstonConfig detailSibling = newMockWinstonConfigWithFlatStringMap(detailSiblingMap);
        when(detailSibling.exists("root")).thenReturn(false);

        WinstonConfig selectors = mock(WinstonConfig.class);
        when(selectors.getSubConfig("details")).thenReturn(detailSibling);

        Map<String, Object> detailExpectationObjectMap = new HashMap<String, Object>();


        Map<String, Object> expectationMap = new HashMap<String, Object>();
        expectationMap.put("details", detailExpectationObjectMap);

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationMap);
        when(expectations.isSimpleConfig("details")).thenReturn(true);

        runValidationTest(dataSource, selectors, expectations);
    }

    @Test
    public void shouldFindValidSiblingMatches() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details><details><name>Perryn</name><sex>F</sex></details><details><name>Ryan</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> detailSiblingMap = new HashMap<String, String>();
        detailSiblingMap.put("root", "/data/details");
        detailSiblingMap.put("name", "name");
        detailSiblingMap.put("sex", "sex");

        WinstonConfig detailSibling = newMockWinstonConfigWithFlatStringMap(detailSiblingMap);
        addMapMockMethodsToConfig(detailSiblingMap, detailSibling);
        when(detailSibling.exists("root")).thenReturn(true);

        Map<String, Object> selectorMap = new HashMap<String, Object>();
        selectorMap.put("details", detailSiblingMap);

        WinstonConfig selectors = mock(WinstonConfig.class);
        when(selectors.getMap()).thenReturn(selectorMap);
        when(selectors.getSubConfig("details")).thenReturn(detailSibling);


        Map<String, Object> detailExpectationObjectMap = new HashMap<String, Object>();
        detailExpectationObjectMap.put("name", "Perryn");
        detailExpectationObjectMap.put("sex", "M");

        WinstonConfig detailExpectations = newMockWinstonConfigWithFlatStringMap(buildFlatStringMap(detailExpectationObjectMap));
        when(detailExpectations.getMap()).thenReturn(detailExpectationObjectMap);

        Map<String, Object> expectationMap = new HashMap<String, Object>();
        expectationMap.put("details", detailExpectationObjectMap);

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationMap);
        when(expectations.isSimpleConfig("details")).thenReturn(true);
        when(expectations.getSubConfig("details")).thenReturn(detailExpectations);


        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'M' for 'sex'");
        successMessages.add("Found 'Perryn' for 'name'");
        assertThat(results.getSuccessMessages(), is(successMessages));
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldComplainIfThereIsNoSiblingMatch() throws Exception {
        Document response = createDocument("<data><details><name>Perryn</name><sex>M</sex></details><details><name>Perryn</name><sex>F</sex></details><details><name>Ryan</name><sex>M</sex></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        Map<String, String> detailSiblingMap = new HashMap<String, String>();
        detailSiblingMap.put("root", "/data/details");
        detailSiblingMap.put("name", "name");
        detailSiblingMap.put("sex", "sex");

        WinstonConfig detailSibling = newMockWinstonConfigWithFlatStringMap(detailSiblingMap);
        addMapMockMethodsToConfig(detailSiblingMap, detailSibling);
        when(detailSibling.exists("root")).thenReturn(true);

        Map<String, Object> selectorMap = new HashMap<String, Object>();
        selectorMap.put("details", detailSiblingMap);

        WinstonConfig selectors = mock(WinstonConfig.class);
        when(selectors.getMap()).thenReturn(selectorMap);
        when(selectors.getSubConfig("details")).thenReturn(detailSibling);

        Map<String, Object> detailExpectationObjectMap = new HashMap<String, Object>();
        detailExpectationObjectMap.put("name", "Ryan");
        detailExpectationObjectMap.put("sex", "F");

        WinstonConfig detailExpectations = newMockWinstonConfigWithFlatStringMap(buildFlatStringMap(detailExpectationObjectMap));
        when(detailExpectations.getMap()).thenReturn(detailExpectationObjectMap);

        Map<String, Object> expectationMap = new HashMap<String, Object>();
        expectationMap.put("details", detailExpectationObjectMap);

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationMap);
        when(expectations.isSimpleConfig("details")).thenReturn(true);
        when(expectations.getSubConfig("details")).thenReturn(detailExpectations);


        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add("The set of nodes at 'details' with xpath '/data/details/' had '3' entries. None of them had the values ['sex' as 'F', 'name' as 'Ryan']");
        List<String> successMessage = new ArrayList<String>();
        assertThat(results.getSuccessMessages(), is(successMessage));
        assertThat(results.getFailureMessages(), is(failureMessages));
    }

    @Test
    public void shouldSupportedNestedSiblings() throws ParserConfigurationException, SAXException, IOException {
        /*
        <data>
            <details>
                <location>Melbourne</location>
                <person>
                    <name>Ryan</name>
                    <sex>M</sex>
                </person>
                <person>
                    <name>Perryn</name>
                    <sex>M</sex>
                </person>
            </details>
            <details>
                <location>Tasmania</location>
                <person>
                    <name>Ryan</name>
                    <sex>M</sex>
                </person>
                <person>
                    <name>Perryn</name>
                    <sex>M</sex>
                </person>
            </details>
        </data>
        */

        Document response = createDocument("<data><details><location>Melbourne</location><person><name>Ryan</name><sex>M</sex></person><person><name>Perryn</name><sex>M</sex></person></details><details><location>Tasmania</location><person><name>Ryan</name><sex>F</sex></person><person><name>Perryn</name><sex>M</sex></person></details></data>");
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getDocument()).thenReturn(response);

        /*
        verify_response:
          details:
            location: Melbourne
            person:
                name: Ryan
                sex: M
        */

        Map<String, Object> personExpectationObjectMap = new HashMap<String, Object>();
        personExpectationObjectMap.put("name", "Ryan");
        personExpectationObjectMap.put("sex", "M");

        WinstonConfig personExpectationConfig = newMockWinstonConfigWithFlatStringMap(buildFlatStringMap(personExpectationObjectMap));
        when(personExpectationConfig.getMap()).thenReturn(personExpectationObjectMap);

        Map<String, Object> detailExpectationObjectMap = new HashMap<String, Object>();
        detailExpectationObjectMap.put("location", "Melbourne");
        detailExpectationObjectMap.put("person", personExpectationObjectMap);

        Map<String, String> detailExpectationObjectFlatStringMap = new HashMap<String, String>();
        detailExpectationObjectFlatStringMap.put("location", "Melbourne");

        WinstonConfig detailExpectations = mock(WinstonConfig.class);
        when(detailExpectations.getMap()).thenReturn(detailExpectationObjectMap);
        when(detailExpectations.getSubConfig("person")).thenReturn(personExpectationConfig);
        when(detailExpectations.getFlatStringMap()).thenReturn(detailExpectationObjectFlatStringMap);
        when(detailExpectations.isSimpleConfig("person")).thenReturn(true);

        Map<String, Object> expectationMap = new HashMap<String, Object>();
        expectationMap.put("details", detailExpectationObjectMap);

        WinstonConfig expectations = mock(WinstonConfig.class);
        when(expectations.getMap()).thenReturn(expectationMap);
        when(expectations.isSimpleConfig("details")).thenReturn(true);
        when(expectations.getSubConfig("details")).thenReturn(detailExpectations);

        /*
        response_selectors:
          details:
            root: /data/details
            Location: location
            person:
                root: person
                Name: name
                Age: Age
        */

        Map<String, Object> personSiblingMap = new HashMap<String, Object>();
        personSiblingMap.put("root", "person");
        personSiblingMap.put("name", "name");
        personSiblingMap.put("sex", "sex");

        WinstonConfig personSiblingConfig = newMockWinstonConfigWithFlatStringMap(buildFlatStringMap(personSiblingMap));
        addMapMockMethodsToConfig(buildFlatStringMap(personSiblingMap), personSiblingConfig);
        when(personSiblingConfig.exists("root")).thenReturn(true);
        when(personSiblingConfig.getMap()).thenReturn(personSiblingMap);

        Map<String, Object> detailSiblingMap = new HashMap<String, Object>();
        detailSiblingMap.put("root", "/data/details");
        detailSiblingMap.put("location", "location");
        detailSiblingMap.put("person", personSiblingMap);

        Map<String, String> detailSiblingFlatStringMap = new HashMap<String, String>();
        detailSiblingFlatStringMap.put("root", "/data/details");
        detailSiblingFlatStringMap.put("location", "location");

        WinstonConfig detailSibling = mock(WinstonConfig.class);
        when(detailSibling.getString("root")).thenReturn("/data/details");
        when(detailSibling.getString("location")).thenReturn("location");
        when(detailSibling.exists("root")).thenReturn(true);
        when(detailSibling.isSimpleConfig("person")).thenReturn(true);
        when(detailSibling.getFlatStringMap()).thenReturn(detailSiblingFlatStringMap);
        when(detailSibling.getSubConfig("person")).thenReturn(personSiblingConfig);


        Map<String, Object> selectorMap = new HashMap<String, Object>();
        selectorMap.put("details", detailSiblingMap);

        WinstonConfig selectors = mock(WinstonConfig.class);
        when(selectors.getMap()).thenReturn(selectorMap);
        when(selectors.getSubConfig("details")).thenReturn(detailSibling);



        ValidationResults results = runValidationTest(dataSource, selectors, expectations);

        List<String> failureMessages = new ArrayList<String>();
        List<String> successMessages = new ArrayList<String>();
        successMessages.add("Found 'M' for 'sex'");
        successMessages.add("Found 'Ryan' for 'name'");
        successMessages.add("Found 'Melbourne' for 'location'");
        assertThat(results.getFailureMessages(), is(failureMessages));
        assertThat(results.getSuccessMessages(), is(successMessages));
    }


    private Document createDocument(String xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private void addMapMockMethodsToConfig(Map<String, String> map, WinstonConfig config) {
        for (String key : map.keySet()) {
            when(config.getString(key)).thenReturn(map.get(key));
        }
    }

    private ValidationResults runValidationTest(DataSource dataSource, WinstonConfig selectors, WinstonConfig expectations) {
        SelectorMatchValidator validator = new SelectorMatchValidator(selectors, expectations);
        return validator.validateAgainst(dataSource);
    }

    private WinstonConfig newMockWinstonConfigWithFlatStringMap(Map<String, String> map) {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getFlatStringMap()).thenReturn(map);
        return config;
    }

    private Map<String, String> buildFlatStringMap(Map<String, Object> expectationsMap) {
        Map<String, String> expectationsFlatStringMap = new HashMap<String, String>();
        for (String key : expectationsMap.keySet()) {
            expectationsFlatStringMap.put(key, (String) expectationsMap.get(key));
        }
        return expectationsFlatStringMap;
    }
}