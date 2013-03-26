package com.thoughtworks.winstonwolfe.config;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class YamlConfig {
    private Map<String, String> map;
    private String pathToConfiguration;

    public YamlConfig(String pathToConfigurationFile) throws FileNotFoundException {
        try {
            File configurationFile = new File(pathToConfigurationFile);

            pathToConfiguration = configurationFile.getParentFile().getPath();

            InputStream input = new FileInputStream(configurationFile);
            Yaml yaml = new Yaml();

            map = (Map<String, String>) yaml.load(input);

        } catch (NullPointerException e) {
            throw new FileNotFoundException(String.format("The file '%s' could not be found", pathToConfigurationFile));
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("[%s] could not be parsed to a Map", pathToConfigurationFile), e);
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

    public File getFile(String key) {
        return new File(pathToConfiguration.concat("/" + get(key)));
    }

    public Boolean exists(String key) {
        return map.containsKey(key);
    }
}