package com.thoughtworks.winstonwolfe.config;

import java.util.Map;

public class MultiFileConfig extends SimpleConfig {
    public MultiFileConfig(Map<String, Object> map, String basePath, ConfigLoader loader) {
        super(map, basePath);
    }
}