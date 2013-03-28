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

public class EndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildHttpServiceEndpoint() throws FileNotFoundException {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getString("endpoint")).thenReturn("http://foo.com");

        EndPointFactory factory = new EndPointFactory(config);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint();
        assertThat(serviceEndPoint, is(instanceOf(HttpServiceEndPoint.class)));
        HttpServiceEndPoint httpServiceEndPoint =  (HttpServiceEndPoint) serviceEndPoint;
        assertThat(httpServiceEndPoint.getUrl(), is("http://foo.com"));
    }
}