package com.thoughtworks.winstonwolfe.datasource;

public class StringDataSource implements DataSource {
    private final String data;

    public StringDataSource(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}