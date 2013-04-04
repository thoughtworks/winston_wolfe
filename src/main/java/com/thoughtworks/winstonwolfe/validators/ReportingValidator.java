package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;

public class ReportingValidator implements ResponseValidator {
    private ResponseValidator validator;
    private HtmlReport report;

    public ReportingValidator(ResponseValidator validator, HtmlReport report) {
        this.validator = validator;
        this.report = report;
    }

    @Override
    public ValidationResults validateAgainst(DataSource actualResponseDataSource) throws Exception {
        ValidationResults results = validator.validateAgainst(actualResponseDataSource);
        report.addResults(results);
        return results;
    }
}