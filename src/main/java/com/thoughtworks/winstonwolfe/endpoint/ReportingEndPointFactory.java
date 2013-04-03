package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.reporting.HtmlReport;

public class ReportingEndPointFactory implements EndPointFactory {
    private HtmlReport report;
    private EndPointFactory endPointFactory;

    public ReportingEndPointFactory(HtmlReport report, EndPointFactory endPointFactory) {
        this.report = report;
        this.endPointFactory = endPointFactory;
    }

    @Override
    public ServiceEndPoint buildEndPoint() {
        return new ReportingEndpoint(endPointFactory.buildEndPoint(), report);
    }
}