package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

public class ExactMatchValidator implements ResponseValidator {
    private final DataSource expected;

    public ExactMatchValidator(DataSource expected) {
        this.expected = expected;
    }

    public void validateAgainst(DataSource actual) {
        String actualResponseData = actual.getData();
        String expectedResponseData = expected.getData();

        if (!expectedResponseData.equals(actualResponseData)) {
            throw new RuntimeException(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", expectedResponseData, actualResponseData));
        }
    }
}