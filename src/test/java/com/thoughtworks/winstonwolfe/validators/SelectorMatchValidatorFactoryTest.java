package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectorMatchValidatorFactoryTest {
    @Test
    public void shouldCreateSelectorMatchValidator() {
        WinstonConfig config = mock(WinstonConfig.class);
        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(true);
        when(config.getSubConfig("verify_response")).thenReturn(subConfig);
        when(config.getSubConfig("response_selectors")).thenReturn(subConfig);
        when(subConfig.getFlatStringMap()).thenReturn(new HashMap<String, String>());

        SelectorMatchValidatorFactory factory = new SelectorMatchValidatorFactory(config);
        SelectorMatchValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(SelectorMatchValidator.class)));
    }
}