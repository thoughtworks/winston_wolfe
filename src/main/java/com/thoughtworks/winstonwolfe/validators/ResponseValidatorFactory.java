package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;

import java.util.Map;

public class ResponseValidatorFactory implements ValidatorFactory {

    private final WinstonConfig config;
    private ExactMatchValidatorFactory exactMatchValidatorFactory;
    private SelectorMatchValidatorFactory selectMatchValidatorFactory;

    public ResponseValidatorFactory(WinstonConfig config, ExactMatchValidatorFactory exactMatchValidatorFactory, SelectorMatchValidatorFactory selectMatchValidatorFactory) {
        this.config = config;
        this.exactMatchValidatorFactory = exactMatchValidatorFactory;
        this.selectMatchValidatorFactory = selectMatchValidatorFactory;
    }

    public ResponseValidator buildValidator() {
        boolean hasResponse = config.exists("compare_response_to");
        boolean hasResponseSelector = config.exists("response_selectors");

        if (hasResponse && hasResponseSelector) {
            throw new RuntimeException("Only compare_response_to or response_selectors can be specified in the test script, not both.");
        }
        if (!hasResponse && !hasResponseSelector) {
            throw new RuntimeException("Either compare_response_to or response_selectors should be specified in the test script.");
        }

        if (hasResponse) {
            return exactMatchValidatorFactory.buildValidator();
        } else {
            return selectMatchValidatorFactory.buildValidator();
        }
    }
}