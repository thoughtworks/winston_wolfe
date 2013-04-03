package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ReportingEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ReportingEndpoint;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportingValidatorFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HtmlReport report;
    private ValidatorFactory validatorFactory;

    @Before
    public void setup() {
        report = mock(HtmlReport.class);
        validatorFactory = mock(ValidatorFactory.class);

        ResponseValidator responseValidator = mock(ResponseValidator.class);
        when(validatorFactory.buildValidator()).thenReturn(responseValidator);
    }

    @Test
    public void shouldDecorateValidatorFactory() throws FileNotFoundException {
        ReportingValidatorFactory reportingValidatorFactory = new ReportingValidatorFactory(report, validatorFactory);
        reportingValidatorFactory.buildValidator();

        verify(validatorFactory).buildValidator();
    }

        @Test
        public void shouldBuildAReportingValidator() {
            ReportingValidatorFactory reportingValidatorFactory = new ReportingValidatorFactory(report, validatorFactory);
            ResponseValidator reportingValidator =  reportingValidatorFactory.buildValidator();

            assertThat(reportingValidator, is(instanceOf(ReportingValidator.class)));
        }
}
