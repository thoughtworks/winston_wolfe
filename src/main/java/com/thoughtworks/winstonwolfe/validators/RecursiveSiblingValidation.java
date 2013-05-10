package com.thoughtworks.winstonwolfe.validators;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.*;

public class RecursiveSiblingValidation implements Validation {
    private final Node node;
    private final String key;
    private final WinstonConfig expectations;
    private final WinstonConfig selectors;
    private final String xpath;
    public static final String XPATH_SEPARATOR = "/";
    private ValidationResults results = new ValidationResults();

    private final List<String> keysToIgnore = new ArrayList<String>(Arrays.asList("root"));

    public RecursiveSiblingValidation(Node node, String key, WinstonConfig expectations, WinstonConfig selectors, String xpath) {
        this.node = node;
        this.key = key;
        this.expectations = expectations;
        this.selectors = selectors;
        this.xpath = xpath;
    }

    @Override
    public void validate() throws XPathExpressionException {
        WinstonConfig siblingExpectations = expectations.getSubConfig(key);

        WinstonConfig siblingSelectors = selectors.getSubConfig(key);
        if (!siblingSelectors.exists("root")) {
            throw new RuntimeException(String.format("Sibling validation '%s' does not contain an element called 'root'", key));
        }
        String siblingRootXpath = siblingSelectors.getString("root");

        NodeList nodesMatchingRoot = (NodeList) XPathFactory.newInstance().newXPath().evaluate(siblingRootXpath, node, XPathConstants.NODESET);
        boolean siblingIsValid = false;
        for (int i = 0; i < nodesMatchingRoot.getLength(); i++) {
            Node node = nodesMatchingRoot.item(i);

            ValidationResults resultsForNode = new ValidationResults();

            Set<String> keys = siblingExpectations.getMap().keySet();
            keys.removeAll(keysToIgnore);
            for (String siblingKey : keys) {
                ValidationFactory validatorFactory = new ValidationFactory(node, siblingKey, siblingExpectations, siblingSelectors, getParentXpathForChildren(siblingRootXpath));
                Validation validation = validatorFactory.buildValidation();
                validation.validate();

                resultsForNode.addValidationResults(validation.getResults());
            }

            if (resultsForNode.hasFailure()) {
                continue;
            }

            siblingIsValid = true;
            results.getSuccessMessages().addAll(resultsForNode.getSuccessMessages());
        }

        if (!siblingIsValid) {
            results.getFailureMessages().add(String.format("The set of nodes at '%s' with xpath '%s' had '%s' entries. None of them had the values %s", key, getParentXpathForChildren(siblingRootXpath), nodesMatchingRoot.getLength(), getSiblingExpectationsNotMetString(siblingExpectations.getFlatStringMap())));
        }

        return;
    }

    private String getParentXpathForChildren (String siblingRootXpath) {
        return xpath + siblingRootXpath + XPATH_SEPARATOR;
    }

    private String getSiblingExpectationsNotMetString(Map<String, String> expectationsMap) {
        Set<String> keys = expectationsMap.keySet();
        keys.removeAll(keysToIgnore);

        List<String> expectationStrings = new ArrayList<String>();
        for (String siblingKey : keys) {
            expectationStrings.add(String.format("'%s' as '%s'", siblingKey, expectationsMap.get(siblingKey)));
        }

        return Arrays.toString(expectationStrings.toArray());
    }

    @Override
    public ValidationResults getResults() {
        return results;
    }
}