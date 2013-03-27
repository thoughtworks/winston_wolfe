package com.thoughtworks.winstonwolfe.selectors;

import com.sun.deploy.util.StringUtils;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectorFactory {

    public Map<String, XPathExpression> buildSelectors(Map<String, String> inputSelectors) {
        List<String> validationFailures = new ArrayList<String>();
        Map<String, XPathExpression> results = new HashMap<String, XPathExpression>();

        

        for (String key : inputSelectors.keySet()) {
            String xpathString = inputSelectors.get(key);
            try {
                results.put(key, XPathFactory.newInstance().newXPath().compile(xpathString));
            } catch (XPathExpressionException e) {
                validationFailures.add(String.format("The selector '%s' has invalid xpath '%s'", key, xpathString));
            }
        }

        if (!validationFailures.isEmpty()) {
        throw new RuntimeException(StringUtils.join(validationFailures, ","));
        }
        return results;
    }
}