package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExactMatchValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnASuccessfulValidationResult() throws Exception {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("<tag>WE ARE THE SAME</tag>");
        when(expected.getData()).thenReturn("<tag>WE ARE THE SAME</tag>");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("The response met expectations");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnASuccessfulValidationResultForSimilarXML() throws Exception {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("<root><tag1>WE ARE</tag1><tag2>SIMILAR</tag2></root>");
        when(expected.getData()).thenReturn("<root><tag2>SIMILAR</tag2><tag1>WE ARE</tag1></root>");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("The response met expectations");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnASuccessfulValidationResultIgnoringWhitespace() throws Exception {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("<root>\n<tag1>WE ARE</tag1>    \n    <tag2>SIMILAR</tag2></root>");
        when(expected.getData()).thenReturn("<root><tag2>SIMILAR</tag2><tag1>WE ARE</tag1></root>");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("The response met expectations");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }


    @Test
    public void shouldReturnAFailedValidationResult() throws Exception {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("<tag>I ARE ACTUAL</tag>");
        when(expected.getData()).thenReturn("<tag>I ARE EXPECTED</tag>");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add(String.format("The expected response did not match the actual response.\n[different] Expected text value 'I ARE ACTUAL' but was 'I ARE EXPECTED' - comparing <tag ...>I ARE ACTUAL</tag> at /tag[1]/text()[1] to <tag ...>I ARE EXPECTED</tag> at /tag[1]/text()[1]\n"));
        assertThat(results.getFailureMessages(), is(failureMessages));
    }
}