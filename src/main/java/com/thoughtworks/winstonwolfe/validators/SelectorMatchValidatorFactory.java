package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

import java.util.Map;

public class SelectorMatchValidatorFactory {
    private WinstonConfig config;

    public SelectorMatchValidatorFactory(WinstonConfig config) {
        this.config = config;
    }

    public SelectorMatchValidator buildValidator() {
        WinstonConfig response_expectations = config.getSubConfig("verify_response");
        WinstonConfig selectors = config.getSubConfig("response_selectors");

        return new SelectorMatchValidator(selectors, response_expectations);
    }
}