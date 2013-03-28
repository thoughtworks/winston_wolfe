package com.thoughtworks.winstonwolfe.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfigLoader implements ConfigLoader {
    @Override
    public WinstonConfig load(String path) throws FileNotFoundException {
        try {
            File configurationFile = new File(path);
            InputStream input = new FileInputStream(configurationFile);

            Yaml yaml = new Yaml();

            return new MultiFileConfig((Map<String, Object>) yaml.load(input), configurationFile.getParentFile().getPath(), this);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("[%s] could not be parsed to a Map", path), e);
        }
    }
}