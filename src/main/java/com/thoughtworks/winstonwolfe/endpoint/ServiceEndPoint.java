package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

import java.io.IOException;

public interface ServiceEndPoint {
    DataSource send(DataSource data) throws IOException;
}