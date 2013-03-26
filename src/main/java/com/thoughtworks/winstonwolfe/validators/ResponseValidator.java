package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

public interface ResponseValidator {
    void validateAgainst(DataSource actualResponseDataSource);
}