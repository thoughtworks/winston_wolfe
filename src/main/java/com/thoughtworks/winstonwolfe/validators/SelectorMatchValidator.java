package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringReader;
import java.util.Map;

public class SelectorMatchValidator implements ResponseValidator {
    private final Map<String, String> expectations;
    private final Map<String, XPathExpression> selectors;

    public SelectorMatchValidator(Map<String, XPathExpression> selectors, Map<String, String> expectations) {
        this.selectors = selectors;
        this.expectations = expectations;
    }

    public SelectorMatchValidator() {
        selectors = null;
        expectations = null;
    }

    @Override
    public void validateAgainst(DataSource actualResponseDataSource) {
        InputSource inputSource = new InputSource(new StringReader(actualResponseDataSource.getData()));

        for (String key : expectations.keySet()) {
            try {
                XPathExpression selector = selectors.get(key);
                String result = selector.evaluate(inputSource);
                String expectedValue = expectations.get(key);

                if (result.isEmpty()) {
                    throw new RuntimeException(
                            String.format("The Xpath identified as '%s' does not exist in the response",
                                    key));
                }
                if (!result.equals(expectedValue)) {
                    throw new RuntimeException(
                            String.format("Expected '%s' for '%s' but found '%s'",
                                    expectedValue, key, result));
                }
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }
}