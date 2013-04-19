package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;

public class ExactMatchValidatorFactory {
    private WinstonConfig config;

    public ExactMatchValidatorFactory(WinstonConfig config) {
        this.config = config;
    }

    public ExactMatchValidator buildValidator() {
        return new ExactMatchValidator(new FileDataSource("compare_response_to", config));
    };
}