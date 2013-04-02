package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;

public class ScriptEndPointFactory {
    private WinstonConfig scriptConfig;
    private NamedEndPointFactory namedFactory;

    public ScriptEndPointFactory(WinstonConfig scriptConfig, NamedEndPointFactory namedFactory) {
        this.scriptConfig = scriptConfig;
        this.namedFactory = namedFactory;
    }

    public ServiceEndPoint buildEndPoint() {
        return namedFactory.buildEndPoint(scriptConfig.getString("send_to"));
    }
}