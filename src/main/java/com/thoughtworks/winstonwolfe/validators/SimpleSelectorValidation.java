package com.thoughtworks.winstonwolfe.validators;

import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SimpleSelectorValidation implements Validation {
    private final Node parentNode;
    private final String key;
    private final String expectedValue;
    private final String selector;

    ValidationResults results = new ValidationResults();

    public SimpleSelectorValidation(Node parentNode, String key, String expectedValue, String selector) {
        this.parentNode = parentNode;
        this.key = key;
        this.expectedValue = expectedValue;
        this.selector = selector;
    }

    public void validate() throws XPathExpressionException {
        if (selector == null) {
            results.getFailureMessages().add(String.format("Expected '%s' to be '%s' but no selector called '%s' was supplied", key, expectedValue, key));
            return;
        }

        String result = XPathFactory.newInstance().newXPath().evaluate(selector, parentNode);
        if (result.isEmpty()) {
            results.getFailureMessages().add(String.format("The Xpath identified as '%s' does not exist in the response", key));
            return;
        }
        if (!result.equals(expectedValue)) {
            results.getFailureMessages().add(String.format("Expected '%s' for '%s' but found '%s'", expectedValue, key, result));
            return;
        }

        results.getSuccessMessages().add(String.format("Found '%s' for '%s'", result, key));
    }

    @Override
    public ValidationResults getResults() {
        return results;
    }
}