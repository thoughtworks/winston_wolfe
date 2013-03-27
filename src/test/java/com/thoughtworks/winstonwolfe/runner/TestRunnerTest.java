package com.thoughtworks.winstonwolfe.runner;

import com.sun.xml.internal.ws.server.EndpointFactory;
import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestRunnerTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void itShouldComplainIfThereIsNoRequestConfig() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Test Script does not contain a 'request' document.");

        Map<String, Object> fakeConfig = new HashMap<String, Object>();

        YamlConfig config = mock(YamlConfig.class);
        when(config.getMap()).thenReturn(fakeConfig);

        EndPointFactory endpointFactory = mock(EndPointFactory.class);

        new TestRunner(config, endpointFactory);
    }

    @Test
    public void itShouldComplainIfTheRequestDocumentIsNotFound() throws IOException {
        expectedException.expect(FileNotFoundException.class);

        Map<String, Object> fakeConfig = new HashMap<String, Object>();
        fakeConfig.put("request", "does_not_exist");

        YamlConfig config = mock(YamlConfig.class);
        when(config.getMap()).thenReturn(fakeConfig);

        EndPointFactory endpointFactory = mock(EndPointFactory.class);

        new TestRunner(config, endpointFactory);
    }

//    @Test
//    public void itShouldSendTheLoadedRequestToTheEndPoint() {
//        OurFile file = mock(OurFile.class);
//        when(file.toString).thenReturn("stuff to send to endpoint");
//
//        Map<String, String> fakeConfig = new HashMap<String, String>();
//        fakeConfig.put("request", file.);
//
//        YamlConfig config = mock(YamlConfig.class);
//        when(config.getMap()).thenReturn(fakeConfig);
//
//        EndPointFactory endpointFactory = mock(EndPointFactory.class);
//
//        new TestRunner(test, endpointFactory);
//    }
}