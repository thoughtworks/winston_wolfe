package com.thoughtworks.winstonwolfe.endpoint;

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

public class ReportingEndPointFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HtmlReport report;
    private EndPointFactory endPointFactory;

    @Before
    public void setup() {
        report = mock(HtmlReport.class);
        endPointFactory = mock(EndPointFactory.class);

        ServiceEndPoint serviceEndPoint = mock(ServiceEndPoint.class);
        when(endPointFactory.buildEndPoint()).thenReturn(serviceEndPoint);
    }

    @Test
    public void shouldDecorateScriptEndPointFactory() throws FileNotFoundException {
        ReportingEndPointFactory reportingEndPointFactory = new ReportingEndPointFactory(report, endPointFactory);
        reportingEndPointFactory.buildEndPoint();

        verify(endPointFactory).buildEndPoint();
    }

    @Test
    public void shouldBuildAReportingEndPoint() {
        ReportingEndPointFactory reportingEndPointFactory = new ReportingEndPointFactory(report, endPointFactory);
        ServiceEndPoint reportingEndPoint = reportingEndPointFactory.buildEndPoint();

        assertThat(reportingEndPoint, is(instanceOf(ReportingEndpoint.class)));
    }
}