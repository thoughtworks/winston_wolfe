package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Scanner;

public class FileDataSource implements DataSource {

    private String key;
    private File file;

    public FileDataSource(String key, WinstonConfig config) {
        this.key = key;
        this.file = config.getFile(key);
    }

    @Override
    public String getData() {
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Unable to find %s file named %s", key, file), e);
        }
    }

    @Override
    public Document getDocument() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            StreamResult result = new StreamResult(new StringWriter());

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            DOMSource source = new DOMSource(documentBuilder.parse(new InputSource(new StringReader(getData()))));
            transformer.transform(source, result);

            return documentBuilder.parse(new InputSource(new StringReader(result.getWriter().toString())));
        } catch (SAXException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        } catch (TransformerException e) {
            throw new RuntimeException("Unable to build an XML document from the supplied input", e);
        }
    }
}