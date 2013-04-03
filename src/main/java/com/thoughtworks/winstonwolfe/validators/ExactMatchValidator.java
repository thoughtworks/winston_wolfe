package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

import java.util.ArrayList;
import java.util.List;

public class ExactMatchValidator implements ResponseValidator {
    private final DataSource expected;

    public ExactMatchValidator(DataSource expected) {
        this.expected = expected;
    }

    public ValidationResults validateAgainst(DataSource actual) {
        String actualResponseData = actual.getData();
        String expectedResponseData = expected.getData();

        List<String> successMessages = new ArrayList<String>();
        List<String> failureMessages = new ArrayList<String>();

        if (!expectedResponseData.equals(actualResponseData)) {
            failureMessages.add(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", expectedResponseData, actualResponseData));
        } else {
            successMessages.add("The response met expectations");
        }

        return new ValidationResults(successMessages, failureMessages);
    }
}