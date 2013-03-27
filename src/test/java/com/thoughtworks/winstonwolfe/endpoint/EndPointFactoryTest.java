package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBuildHttpServiceEndpoint() throws FileNotFoundException {
        Map<String, Object> fakeConfig = new HashMap<String, Object>();
        fakeConfig.put("endpoint", "http://foo.com");

        YamlConfig config = mock(YamlConfig.class);
        when(config.getMap()).thenReturn(fakeConfig);

        EndPointFactory factory = new EndPointFactory(config);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint();
        assertThat(serviceEndPoint, is(instanceOf(HttpServiceEndPoint.class)));
        HttpServiceEndPoint httpServiceEndPoint =  (HttpServiceEndPoint) serviceEndPoint;
        assertThat(httpServiceEndPoint.getUrl(), is("http://foo.com"));
    }

    @Test
    public void shouldComplainBitterlyIfNoEndPointConfig() throws FileNotFoundException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("No endpoint specified in Config");

        Map<String, Object> fakeConfig = new HashMap<String, Object>();
        fakeConfig.put("derp", "herp");

        YamlConfig config = mock(YamlConfig.class);
        when(config.getMap()).thenReturn(fakeConfig);

        new EndPointFactory(config).buildEndPoint();
    }
}