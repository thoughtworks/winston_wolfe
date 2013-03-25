package com.thoughtworks.winstonwolfe.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfig {
    private Map<String, String> map;

    public YamlConfig(String pathToConfiguration) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(pathToConfiguration));
        Yaml yaml = new Yaml();
        try {
            map = (Map<String, String>) yaml.load(input);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("[%s] could not be parsed to a Map", pathToConfiguration), e);
        }
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String get(String key) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(String.format("No %s specified in yaml", key));
        }
        return map.get(key);
    }
}