package com.thoughtworks.winstonwolfe.datasource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class StringDataSource implements DataSource {
    private final String data;

    public StringDataSource(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public Document getDocument()  {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(data)));
        } catch (SAXException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        }
    }
}