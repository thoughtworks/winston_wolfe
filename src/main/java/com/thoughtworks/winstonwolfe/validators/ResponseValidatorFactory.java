package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;

import java.util.Map;

public class ResponseValidatorFactory {

    private final WinstonConfig config;

    public ResponseValidatorFactory(WinstonConfig config) {
        this.config = config;
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
            return new ExactMatchValidator(new FileDataSource("compare_response_to", config));
        } else {
            Map<String, String> selectors = config.getSubConfig("response_selectors").getFlatStringMap();
            Map<String, String> response_expectations = config.getSubConfig("verify_response").getFlatStringMap();;
            return new SelectorMatchValidator(selectors, response_expectations);
        }
    }
}