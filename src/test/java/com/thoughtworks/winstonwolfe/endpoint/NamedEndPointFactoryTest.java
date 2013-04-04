package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NamedEndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildHttpServiceEndpointWhenProvidedHTTPUrl() throws FileNotFoundException {
        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(subConfig.exists("http_url")).thenReturn(true);
        WinstonConfig endpointConfig = mock(WinstonConfig.class);
        when(endpointConfig.getSubConfig("service")).thenReturn(subConfig);

        NamedEndPointFactory factory = new NamedEndPointFactory(endpointConfig);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint("service");
        assertThat(serviceEndPoint, is(instanceOf(HttpServiceEndPoint.class)));
    }

    @Test
    public void shouldBuildJmsServiceEndpointWhenNoHTTPUrl() {
        WinstonConfig subConfig = mock(WinstonConfig.class);
        when(subConfig.exists("http_url")).thenReturn(false);
        WinstonConfig endpointConfig = mock(WinstonConfig.class);
        when(endpointConfig.getSubConfig("service")).thenReturn(subConfig);

        NamedEndPointFactory factory = new NamedEndPointFactory(endpointConfig);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint("service");
        assertThat(serviceEndPoint, is(instanceOf(JmsServiceEndPoint.class)));
    }
}