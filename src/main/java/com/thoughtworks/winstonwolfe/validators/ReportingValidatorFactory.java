package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.reporting.HtmlReport;

public class ReportingValidatorFactory implements ValidatorFactory{
    private HtmlReport report;
    private ValidatorFactory validatorFactory;

    public ReportingValidatorFactory(HtmlReport report, ValidatorFactory validatorFactory) {
        this.report = report;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public ResponseValidator buildValidator() {
        return new ReportingValidator(validatorFactory.buildValidator(), report);
    }
}