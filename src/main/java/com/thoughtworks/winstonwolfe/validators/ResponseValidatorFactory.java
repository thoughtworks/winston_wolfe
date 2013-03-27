package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;

import java.util.Map;

public class ResponseValidatorFactory {

    private final YamlConfig config;

    public ResponseValidatorFactory(YamlConfig config) {
        this.config = config;
    }

    public ResponseValidator buildValidator() {
        boolean hasResponse = config.exists("response");
        boolean hasResponseSelector = config.exists("response_selectors");

        if (hasResponse && hasResponseSelector) {
            throw new RuntimeException("Only response or response_selectors can be specified in the test script, not both.");
        }
        if (!hasResponse && !hasResponseSelector) {
            throw new RuntimeException("Either response or response_selectors should be specified in the test script.");
        }

        if (hasResponse) {
            return new ExactMatchValidator(new FileDataSource("response", config));
        } else {
            Map<String, String> selectors = config.getKeyValueMap("response_selectors");
            Map<String, String> response_expectations = config.getKeyValueMap("response_expectations");
            return new SelectorMatchValidator(selectors, response_expectations);
        }
    }
}