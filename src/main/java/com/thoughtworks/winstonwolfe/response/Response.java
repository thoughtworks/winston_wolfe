package com.thoughtworks.winstonwolfe.response;

import com.thoughtworks.winstonwolfe.datasource.DataSource;

public class Response {
    private DataSource dataSource;

    public Response(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}