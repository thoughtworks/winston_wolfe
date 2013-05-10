package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public class ValidationFactory {
    private final Node node;
    private final String key;
    private final WinstonConfig expectations;
    private final WinstonConfig selectors;
    private final String xpath;

    public ValidationFactory(Node node, String key, WinstonConfig expectations, WinstonConfig selectors, String xpath) {
        this.node = node;
        this.key = key;
        this.expectations = expectations;
        this.selectors = selectors;
        this.xpath = xpath;
    }

    public Validation buildValidation() throws XPathExpressionException {
        if (expectations.isSimpleConfig(key)) {
            return new RecursiveSiblingValidation(node, key, expectations, selectors, xpath);
        } else {
            String expectedValue = expectations.getFlatStringMap().get(key);
            String selector = selectors.getFlatStringMap().get(key);

            return new SimpleSelectorValidation(node, key, expectedValue, selector);
        }
    }
}