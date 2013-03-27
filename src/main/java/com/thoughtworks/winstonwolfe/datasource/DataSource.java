package com.thoughtworks.winstonwolfe.datasource;

import org.w3c.dom.Document;

public interface DataSource {
    String getData();

    Document getDocument();
}