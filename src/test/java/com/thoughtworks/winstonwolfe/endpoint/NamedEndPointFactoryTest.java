package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NamedEndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildHttpServiceEndpoint() throws FileNotFoundException {
        WinstonConfig endpointConfig = mock(WinstonConfig.class);
        when(endpointConfig.getString("jetty_service")).thenReturn("http://foo.com");

        NamedEndPointFactory factory = new NamedEndPointFactory(endpointConfig);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint("jetty_service");
        assertThat(serviceEndPoint, is(instanceOf(HttpServiceEndPoint.class)));
        HttpServiceEndPoint httpServiceEndPoint =  (HttpServiceEndPoint) serviceEndPoint;
        assertThat(httpServiceEndPoint.getUrl(), is("http://foo.com"));
    }
}