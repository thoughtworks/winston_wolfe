package com.thoughtworks.winstonwolfe.script;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.endpoint.ScriptEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.validators.ResponseValidator;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScriptTest {

    private Script script;
    private ResponseValidator validator;
    private DataSource actualResponseDataSource;
    private ServiceEndPoint endPoint;
    private DataSource requestDataSource;

    @Before
    public void setup() throws IOException {
        actualResponseDataSource = mock(DataSource.class);

        ScriptEndPointFactory scriptEndPointFactory = mock(ScriptEndPointFactory.class);
        endPoint = mock(ServiceEndPoint.class);
        when(scriptEndPointFactory.buildEndPoint()).thenReturn(endPoint);
        when(endPoint.send(any(DataSource.class))).thenReturn(actualResponseDataSource);


        ResponseValidatorFactory responseValidatorFactory = mock(ResponseValidatorFactory.class);
        validator = mock(ResponseValidator.class);
        when(responseValidatorFactory.buildValidator()).thenReturn(validator);

        requestDataSource = mock(DataSource.class);
        script = new Script(scriptEndPointFactory, requestDataSource, responseValidatorFactory);
    }

    @Test
    public void shouldSendTheRequestToTheEndPoint() throws IOException {
        script.run();
        verify(endPoint).send(requestDataSource);
    }

    @Test
    public void shouldValidateTheResponse() throws IOException {
        script.run();

        verify(validator).validateAgainst(actualResponseDataSource);
    }
}