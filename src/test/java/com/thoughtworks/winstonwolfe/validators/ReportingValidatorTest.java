package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.endpoint.ReportingEndpoint;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportingValidatorTest {

    private HtmlReport report;
    private ResponseValidator validator;
    private ValidationResults results;
    private DataSource dataSource;

    @Before
    public void setup() throws Exception {
        report = mock(HtmlReport.class);
        dataSource = mock(DataSource.class);
        validator = mock(ResponseValidator.class);
        results = mock(ValidationResults.class);
        when(validator.validateAgainst(any(DataSource.class))).thenReturn(results);

    }

    @Test
    public void shouldDecorateValidator() throws Exception {
        ReportingValidator reportingValidator = new ReportingValidator(validator, report);
        assertThat(reportingValidator.validateAgainst(dataSource), is(results));

        verify(validator).validateAgainst(dataSource);
    }

    @Test
    public void shouldReportResultsOfValidation() throws Exception {
        ReportingValidator reportingValidator = new ReportingValidator(validator, report);
        reportingValidator.validateAgainst(dataSource);

        verify(report).addResults(results);
    }
}