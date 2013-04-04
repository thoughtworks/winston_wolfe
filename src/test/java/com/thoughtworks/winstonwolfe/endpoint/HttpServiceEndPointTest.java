package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpServiceEndPointTest {
    @Test
    public void shouldSetTheUrl() {
        WinstonConfig config = mock(WinstonConfig.class);
        when(config.getString("http_url")).thenReturn("http://derp.com");

        HttpServiceEndPoint httpServiceEndPoint = new HttpServiceEndPoint(config);

        assertThat(httpServiceEndPoint.getUrl(), is("http://derp.com"));
    }
}