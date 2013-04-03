package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;

import java.io.IOException;

public class ReportingEndpoint implements ServiceEndPoint {
    private ServiceEndPoint endpoint;
    private HtmlReport report;

    public ReportingEndpoint(ServiceEndPoint endpoint, HtmlReport report) {
        this.endpoint = endpoint;
        this.report = report;
    }

    @Override
    public DataSource send(DataSource request) throws IOException {
        report.setRequest(request.getData());
        DataSource response = endpoint.send(request);
        report.setResponse(response.getData());

        return response;
    }
}