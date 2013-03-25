package com.thoughtworks.winstonwolfe.request;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

public class Request {
    private DataSource dataSource;

    public Request(DataSource dataSource) {
        this.dataSource = dataSource;
    }

//    public void sendTo(ServiceEndPoint endPoint) throws IOException {
//        endPoint.send(dataSource.getData());
//    }

    public DataSource getDataSource() {
        return dataSource;
    }
}