package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExactMatchValidatorFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreateExactMatchValidator() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(true);

        ExactMatchValidatorFactory factory = new ExactMatchValidatorFactory(config);
        ExactMatchValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(ExactMatchValidator.class)));
    }
}