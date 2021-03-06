package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResponseValidatorFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldComplainIfNeitherResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Either compare_response_to or response_selectors should be specified in the test script.");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(false);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config, mock(ExactMatchValidatorFactory.class), mock(SelectorMatchValidatorFactory.class));
        factory.buildValidator();
    }

    @Test
    public void shouldComplainIfBothResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Only compare_response_to or response_selectors can be specified in the test script, not both.");

        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(true);
        when(config.exists("response_selectors")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config, mock(ExactMatchValidatorFactory.class), mock(SelectorMatchValidatorFactory.class));
        factory.buildValidator();
    }

    @Test
    public void shouldBuildAExactMatchValidator() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(true);
        when(config.exists("response_selectors")).thenReturn(false);

        ExactMatchValidatorFactory exactMatchValidatorFactory = mock(ExactMatchValidatorFactory.class);
        SelectorMatchValidatorFactory selectorMatchValidatorFactory = mock(SelectorMatchValidatorFactory.class);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config, exactMatchValidatorFactory, selectorMatchValidatorFactory);
        factory.buildValidator();
        verify(exactMatchValidatorFactory).buildValidator();
    }

    @Test
    public void shouldBuildASelectorMatchValidation() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.exists("compare_response_to")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(true);

        ExactMatchValidatorFactory exactMatchValidatorFactory = mock(ExactMatchValidatorFactory.class);
        SelectorMatchValidatorFactory selectorMatchValidatorFactory = mock(SelectorMatchValidatorFactory.class);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config, exactMatchValidatorFactory, selectorMatchValidatorFactory);
        factory.buildValidator();
        verify(selectorMatchValidatorFactory).buildValidator();
    }
}