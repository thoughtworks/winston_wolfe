package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.HttpServiceEndPoint;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Map;

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
        YamlConfig config = mock(YamlConfig.class);
        when(config.exists("response")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        ResponseValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(ExactMatchValidator.class)));
    }

    @Test
    public void shouldComplainIfNeitherResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Either response or response_selectors should be specified in the test script.");

        YamlConfig config = mock(YamlConfig.class);
        when(config.exists("response")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(false);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        factory.buildValidator();
    }

    @Test
    public void shouldComplainIfBothResponseFileOrSelectorFilesAreSpecified() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Only response or response_selectors can be specified in the test script, not both.");

        YamlConfig config = mock(YamlConfig.class);
        when(config.exists("response")).thenReturn(true);
        when(config.exists("response_selectors")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        factory.buildValidator();
    }

    @Test
    public void shouldCreateSelectorMatchValidatorIfConfigDoesNotSpecifyAResponseFile() {
        YamlConfig config = mock(YamlConfig.class);
        when(config.exists("response")).thenReturn(false);
        when(config.exists("response_selectors")).thenReturn(true);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(config);
        ResponseValidator validator = factory.buildValidator();
        assertThat(validator, is(instanceOf(SelectorMatchValidator.class)));
    }
}