package com.thoughtworks.winstonwolfe.request;

import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;

import java.io.IOException;

public class Request {

    private FileDataSource dataSource;

    public Request(FileDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void sendTo(ServiceEndPoint endPoint) throws IOException {
        endPoint.send(dataSource.getData());
    }
}