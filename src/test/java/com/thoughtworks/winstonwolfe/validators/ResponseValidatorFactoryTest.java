package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.config.YamlConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseValidatorFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreateExactMatchValidatorIfConfigSpecifiesAResponseFile() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("response")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        ResponseValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(ExactMatchValidator.class)));
    }

    @Test
    public void shouldComplainIfNeitherResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Either response or response_selectors should be specified in the test script.");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("response")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(false);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        factory.buildValidator();
    }

    @Test
    public void shouldComplainIfBothResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Only response or response_selectors can be specified in the test script, not both.");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("response")).thenReturn(true);
        when(config.exists("response_selectors")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        factory.buildValidator();
    }

    @Test
    public void shouldCreateSelectorMatchValidatorIfConfigDoesNotSpecifyAResponseFile() {
        WinstonConfig config = mock(WinstonConfig.class);
        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(config.exists("response")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(true);
        when(config.getSubConfig("response_expectations")).thenReturn(subConfig);
        when(config.getSubConfig("response_selectors")).thenReturn(subConfig);
        when(subConfig.getFlatStringMap()).thenReturn(new HashMap<String, String>());

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        ResponseValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(SelectorMatchValidator.class)));
    }
}