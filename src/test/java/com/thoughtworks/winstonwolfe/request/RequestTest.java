package com.thoughtworks.winstonwolfe.request;

import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RequestTest {

    @Test
    public void shouldSendDataToEndpoint() throws IOException {

        FileDataSource dataSource = mock(FileDataSource.class);
        when(dataSource.getData()).thenReturn("DATA");
        ServiceEndPoint endPoint = mock(ServiceEndPoint.class);

//        new Request(dataSource).sendTo(endPoint);

        verify(endPoint).send("DATA");
    }
}