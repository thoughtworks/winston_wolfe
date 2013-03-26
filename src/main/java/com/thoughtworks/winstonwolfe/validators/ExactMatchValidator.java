package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

public class ExactMatchValidator {
    public void validate(DataSource actual, DataSource expected) {
        String actualResponseData = actual.getData();
        String expectedResponseData = expected.getData();

        if (!expectedResponseData.equals(actualResponseData)) {
            throw new RuntimeException(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", expectedResponseData, actualResponseData));
        }
    }
}