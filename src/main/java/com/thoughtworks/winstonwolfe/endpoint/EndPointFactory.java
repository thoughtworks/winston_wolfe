package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

public class EndPointFactory {

    private WinstonConfig config;

    public EndPointFactory(WinstonConfig config) {
        this.config = config;
    }

    public ServiceEndPoint buildEndPoint(String endpoint) {
        return new HttpServiceEndPoint(config.getString(endpoint));
    }
}