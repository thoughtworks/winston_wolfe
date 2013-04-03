package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScriptEndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldObtainEndpointNamedInScriptConfig() throws FileNotFoundException {
        ServiceEndPoint builtEndPoint = mock(ServiceEndPoint.class);

        NamedEndPointFactory namedFactory = mock(NamedEndPointFactory.class);
        when(namedFactory.buildEndPoint("ENDPOINT NAME")).thenReturn(builtEndPoint);

        WinstonConfig scriptConfig = mock(WinstonConfig.class);
        when(scriptConfig.getString("send_to")).thenReturn("ENDPOINT NAME");

        EndPointFactory factory = new ScriptEndPointFactory(scriptConfig, namedFactory);
        ServiceEndPoint serviceEndPoint = factory.buildEndPoint();
        assertThat(serviceEndPoint, is(builtEndPoint));
    }
}