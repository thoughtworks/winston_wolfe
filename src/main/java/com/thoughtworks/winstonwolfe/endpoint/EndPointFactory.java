package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.YamlConfig;

import java.util.Map;

public class EndPointFactory {

    private Map<String, String> config;

    public EndPointFactory(YamlConfig config) {
        this.config = config.getMap();
    }

    public ServiceEndPoint buildEndPoint() {
        if( !config.containsKey("endpoint")) {
            throw new RuntimeException("No endpoint specified in Config") ;
        }
        return new HttpServiceEndPoint(config.get("endpoint"));
    }

}