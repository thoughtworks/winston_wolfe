package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.response.Response;

public class ExactMatchValidator {
    public void validate(Response actualResponse, Response expectedResponse) {
        String actualResponseData = actualResponse.getDataSource().getData();
        String expectedResponseData = expectedResponse.getDataSource().getData();

        if (!expectedResponseData.equals(actualResponseData)) {
            throw new RuntimeException(String.format("The expected response did not match the actual response.\nExpected:\n'%s'\nActual:\n'%s'", expectedResponse.getDataSource().getData(), actualResponse.getDataSource().getData()));
        }
    }
}