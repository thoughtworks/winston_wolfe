package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectorMatchValidator implements ResponseValidator {
    private final Map<String, String> expectations;
    private final Map<String, String> selectors;

    public SelectorMatchValidator(Map<String, String> selectors, Map<String, String> expectations) {
        this.selectors = selectors;
        this.expectations = expectations;
    }

    @Override
    public void validateAgainst(DataSource actualResponseDataSource) {
        List<String> validationFailures = new ArrayList<String>();

        Document document = actualResponseDataSource.getDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        for (String key : expectations.keySet()) {
            try {
                String expectedValue = expectations.get(key);

                String selector = selectors.get(key);
                if (selector == null) {
                    validationFailures.add(String.format("Expected '%s' to be '%s' but no selector called '%s' was supplied", key, expectedValue, key));
                    continue;
                }

                String result = xpath.evaluate(selector, document);
                if (result.isEmpty()) {
                    validationFailures.add(String.format("The Xpath identified as '%s' does not exist in the response", key));
                    continue;
                }
                if (!result.equals(expectedValue)) {
                    validationFailures.add(String.format("Expected '%s' for '%s' but found '%s'", expectedValue, key, result));
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                validationFailures.add(String.format("The xpath '%s' is not valid. Refer to the horrible stack trace on the console.", key));
            }
        }

        if (!validationFailures.isEmpty()) {
            throw new RuntimeException(Arrays.toString(validationFailures.toArray()));
        }
    }
}