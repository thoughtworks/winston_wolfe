package com.thoughtworks.winstonwolfe.script;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.validators.ResponseValidator;
import com.thoughtworks.winstonwolfe.validators.ValidatorFactory;
import com.thoughtworks.winstonwolfe.validators.ValidationResults;
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
    private ValidationResults validationResults;

    @Before
    public void setup() throws Exception {
        actualResponseDataSource = mock(DataSource.class);

        EndPointFactory endPointFactory = mock(EndPointFactory.class);
        endPoint = mock(ServiceEndPoint.class);
        when(endPointFactory.buildEndPoint()).thenReturn(endPoint);
        when(endPoint.send(any(DataSource.class))).thenReturn(actualResponseDataSource);


       ValidatorFactory responseValidatorFactory = mock(ValidatorFactory.class);
        validator = mock(ResponseValidator.class);
        when(responseValidatorFactory.buildValidator()).thenReturn(validator);

        validationResults = mock(ValidationResults.class);
        when(validator.validateAgainst(any(DataSource.class))).thenReturn(validationResults);

        requestDataSource = mock(DataSource.class);
        script = new Script(endPointFactory, requestDataSource, responseValidatorFactory);
    }

    @Test
    public void shouldSendTheRequestToTheEndPoint() throws Exception {
        script.run();
        verify(endPoint).send(requestDataSource);
    }

    @Test
    public void shouldValidateTheResponse() throws Exception {
        script.run();

        verify(validator).validateAgainst(actualResponseDataSource);
    }

    @Test
    public void shouldCheckTheReponsePassedValidation() throws Exception {
        script.run();
        verify(validationResults).assertSuccess();
    }
}