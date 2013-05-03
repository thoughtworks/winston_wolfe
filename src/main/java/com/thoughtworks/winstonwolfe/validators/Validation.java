package com.thoughtworks.winstonwolfe.validators;

import javax.xml.xpath.XPathExpressionException;

interface Validation {
    void validate() throws XPathExpressionException;

    ValidationResults getResults();
}