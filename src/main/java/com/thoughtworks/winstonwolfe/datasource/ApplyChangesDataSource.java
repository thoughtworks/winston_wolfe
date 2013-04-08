package com.thoughtworks.winstonwolfe.datasource;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;
import java.util.Map;

public class ApplyChangesDataSource implements DataSource {
    private final Map<String, String> selectors;
    private final Map<String, String> changes;
    private final DataSource originalDataSource;

    public ApplyChangesDataSource(Map<String, String> selectors, Map<String, String> changes, DataSource originalDataSource) {
        this.selectors = selectors;
        this.changes = changes;
        this.originalDataSource = originalDataSource;
    }

    @Override
    public String getData() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            StreamResult result = new StreamResult(new StringWriter());

            DOMSource source = new DOMSource(getDocument());
            transformer.transform(source, result);

            return result.getWriter().toString();
        } catch (TransformerException e) {
            throw new RuntimeException("Could not convert document to string.", e);
        }
    }

    @Override
    public Document getDocument() {
        Document document = originalDataSource.getDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        for (String key : changes.keySet()) {
            try {
                String newValue = changes.get(key);

                String selector = selectors.get(key);
                if (selector == null) {
                    throw new RuntimeException(String.format("Wanted to change '%s' to '%s' but no selector called '%s' was supplied", key, newValue, key));
                }
                NodeList result = (NodeList) xpath.evaluate(selector, document, XPathConstants.NODESET);
                if (result.getLength() < 1) {
                    throw new RuntimeException(String.format("The Xpath identified as '%s' does not exist in the request", key));
                }
                result.item(0).setTextContent(newValue);
            } catch (XPathExpressionException e) {
                throw new RuntimeException(String.format("The xpath '%s' is not valid.", key), e);
            }
        }

        return document;
    }
}