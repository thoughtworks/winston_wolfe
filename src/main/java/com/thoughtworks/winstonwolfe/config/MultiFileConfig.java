package com.thoughtworks.winstonwolfe.config;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class MultiFileConfig extends SimpleConfig {

    private ConfigLoader loader;

    public MultiFileConfig(Map<String, Object> map, String basePath, ConfigLoader loader) throws FileNotFoundException {
        super(map, basePath);
        this.loader = loader;
        if (exists("import_files")) {
            List<String> files = getList("import_files");
            for (String file : files) {
                WinstonConfig fileConfig = loader.load(basePath + "/" + file);
                map.putAll(fileConfig.getMap());
            }
            map.remove("import_files");
        }
    }

    public WinstonConfig getSubConfig(String key) {
        try {
            return new MultiFileConfig((Map<String, Object>) typeSafeGet(key, Map.class), basePath, loader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Couldn't find file '%s' referenced by configuration key '%s'", e.getMessage(), key), e);
        }
    }
}