package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

public class NamedEndPointFactory {

    private WinstonConfig config;

    public NamedEndPointFactory(WinstonConfig config) {
        this.config = config;
    }

    public ServiceEndPoint buildEndPoint(String endpoint) {
        WinstonConfig endpointConfig = config.getSubConfig(endpoint);
        if (endpointConfig.exists("http_url")) {
            return new HttpServiceEndPoint(endpointConfig);
        }
        return new JmsServiceEndPoint(endpointConfig);
    }
}