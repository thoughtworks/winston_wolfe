package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExactMatchValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowAnExceptionWhenResponsesDontMatch() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", "I ARE EXPECTED", "I ARE ACTUAL"));

        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("I ARE ACTUAL");
        when(expected.getData()).thenReturn("I ARE EXPECTED");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        validator.validateAgainst(actual);
    }

    @Test
    public void shouldNotThrowAnExceptionWhenMatching() {
        DataSource expected = mock(DataSource.class);
        DataSource actual = mock(DataSource.class);

        when(actual.getData()).thenReturn("WE ARE THE SAME");
        when(expected.getData()).thenReturn("WE ARE THE SAME");

        ExactMatchValidator validator = new ExactMatchValidator(expected);
        validator.validateAgainst(actual);
    }
}