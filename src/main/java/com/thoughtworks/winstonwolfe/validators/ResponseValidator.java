package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface ResponseValidator {
    ValidationResults validateAgainst(DataSource actualResponseDataSource) throws IOException, SAXException, Exception;
}