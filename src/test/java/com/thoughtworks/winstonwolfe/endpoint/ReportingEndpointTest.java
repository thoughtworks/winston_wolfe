package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportingEndpointTest {

    private HtmlReport report;
    private DataSource responseDataSource;
    private ServiceEndPoint endpoint;
    private DataSource dataSource;

    @Before
    public void setup() throws IOException {
        report = mock(HtmlReport.class);
        responseDataSource = mock(DataSource.class);
        when(responseDataSource.getData()).thenReturn("herp");

        endpoint = mock(ServiceEndPoint.class);
        when(endpoint.send(any(DataSource.class))).thenReturn(responseDataSource);

        dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn("derp");
    }
    @Test
    public void shouldReportRequestsSentViaEndpoint() throws IOException {
        ReportingEndpoint reportingEndpoint = new ReportingEndpoint(endpoint, report);
        reportingEndpoint.send(dataSource);

        verify(report).setRequest("derp");
    }

    @Test
    public void shouldDecorateEndpoint() throws IOException {
        ReportingEndpoint reportingEndpoint = new ReportingEndpoint(endpoint, report);
        assertThat(reportingEndpoint.send(dataSource), is(responseDataSource));

        verify(endpoint).send(dataSource);
    }

    @Test
    public void shouldReportResponsesReceivedViaEndpoint() throws IOException {
        ReportingEndpoint reportingEndpoint = new ReportingEndpoint(endpoint, report);
        reportingEndpoint.send(dataSource);

        verify(report).setResponse("herp");
    }
}