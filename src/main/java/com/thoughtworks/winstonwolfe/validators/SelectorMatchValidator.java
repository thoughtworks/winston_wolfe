package com.thoughtworks.winstonwolfe.validators;

import com.sun.deploy.util.StringUtils;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectorMatchValidator implements ResponseValidator {
    private final Map<String, String> expectations;
    private final Map<String, XPathExpression> selectors;

    public SelectorMatchValidator(Map<String, XPathExpression> selectors, Map<String, String> expectations) {
        this.selectors = selectors;
        this.expectations = expectations;
    }

    public SelectorMatchValidator() {
        this.selectors = null;
        this.expectations = null;
    }

    @Override
    public void validateAgainst(DataSource actualResponseDataSource) {
        List<String> validationFailures = new ArrayList<String>();

        for (String key : expectations.keySet()) {
            InputSource inputSource = new InputSource(new StringReader(actualResponseDataSource.getData()));

            try {
                String expectedValue = expectations.get(key);

                XPathExpression selector = selectors.get(key);
                if (selector == null) {
                    validationFailures.add(String.format("Expected '%s' to be '%s' but no selector called '%s' was supplied", key, expectedValue, key));
                    continue;
                }

                String result = selector.evaluate(inputSource);
                if (result.isEmpty()) {
                    validationFailures.add(String.format("The Xpath identified as '%s' does not exist in the response", key));
                    continue;
                }
                if (!result.equals(expectedValue)) {
                    validationFailures.add(String.format("Expected '%s' for '%s' but found '%s'", expectedValue, key, result));
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }

        if (!validationFailures.isEmpty()) {
            throw new RuntimeException(StringUtils.join(validationFailures, "\n"));
        }
    }
}