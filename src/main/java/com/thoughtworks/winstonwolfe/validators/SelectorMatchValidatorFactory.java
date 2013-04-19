package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

import java.util.Map;

public class SelectorMatchValidatorFactory {
    private WinstonConfig config;

    public SelectorMatchValidatorFactory(WinstonConfig config) {
        this.config = config;
    }

    public SelectorMatchValidator buildValidator() {
        Map<String, String> selectors = config.getSubConfig("response_selectors").getFlatStringMap();
        Map<String, String> response_expectations = config.getSubConfig("verify_response").getFlatStringMap();

        return new SelectorMatchValidator(selectors, response_expectations);
    }
}