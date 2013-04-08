package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

import java.util.Map;

public class DataSourceFactory {
    private WinstonConfig config;

    public DataSourceFactory(WinstonConfig config) {
        this.config = config;
    }

    public DataSource buildDataSource() {
        FileDataSource fileDataSource = new FileDataSource("read", config);

        if (config.exists("apply_changes")) {
            Map<String, String> selectors = config.getSubConfig("request_selectors").getFlatStringMap();
            Map<String, String> changes = config.getSubConfig("apply_changes").getFlatStringMap();
            return new ApplyChangesDataSource(selectors, changes, fileDataSource);
        } else {
            return fileDataSource;
        }
    }
}