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
    public void shouldReturnASuccessfulValidationResult() {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("WE ARE THE SAME");
        when(expected.getData()).thenReturn("WE ARE THE SAME");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> successMessages = new ArrayList<String>();
        successMessages.add("The response met expectations");
        assertThat(results.getSuccessMessages(), is(successMessages));
    }

    @Test
    public void shouldReturnAFailedValidationResult() {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("I ARE ACTUAL");
        when(expected.getData()).thenReturn("I ARE EXPECTED");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        ValidationResults results = validator.validateAgainst(actual);

        List<String> failureMessages = new ArrayList<String>();
        failureMessages.add(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", "I ARE EXPECTED", "I ARE ACTUAL"));
        assertThat(results.getFailureMessages(), is(failureMessages));
    }
}