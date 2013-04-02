package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

public class NamedEndPointFactory {

    private WinstonConfig config;

    public NamedEndPointFactory(WinstonConfig config) {
        this.config = config;
    }

    public ServiceEndPoint buildEndPoint(String endpoint) {
        return new HttpServiceEndPoint(config.getString(endpoint));
    }
}