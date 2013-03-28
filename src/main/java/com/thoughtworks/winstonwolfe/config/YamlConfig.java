package com.thoughtworks.winstonwolfe.config;

import org.yaml.snakeyaml.Yaml;
import sun.plugin2.main.client.MacOSXKeyHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlConfig {
    private Map<String, Object> map;
    private String pathToConfiguration;

    public YamlConfig(String pathToConfigurationFile) throws FileNotFoundException {
        try {
            File configurationFile = new File(pathToConfigurationFile);

            pathToConfiguration = configurationFile.getParentFile().getPath();

            InputStream input = new FileInputStream(configurationFile);
            Yaml yaml = new Yaml();

            map = (Map<String, Object>) yaml.load(input);

        } catch (NullPointerException e) {
            throw new FileNotFoundException(String.format("The file '%s' could not be found", pathToConfigurationFile));
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("[%s] could not be parsed to a Map", pathToConfigurationFile), e);
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public String get(String key) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(String.format("No %s specified in yaml", key));
        }
        return (String) map.get(key);
    }

    public List<String> getList(String key) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(String.format("Element '%s' could not be found in the yaml", key));
        }

        try {
            return (List<String>) map.get(key);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("Element '%s' is not a list.", key), e);
        }
    }

    public List<String> getList(String key, Map<String, Object> map) {
        if (!map.containsKey(key)) {
            throw new RuntimeException(String.format("Element '%s' could not be found in the yaml", key));
        }

        try {
            return (List<String>) map.get(key);
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("Element '%s' is not a list.", key), e);
        }
    }

    public static Map<String, String> stripOutImportFilesFromMap(Map<String, Object> source) {
        //TODO: implement me?
//        for (String childKey : child.keySet()) {
//            if (key.equals("import_files")) {
//                kv.put(childKey, (String) child.get(childKey));
//            }
//        }
        return null;
    }

    public static Map<String, String> mergeMap(Map<String, String> a, Map <String, String> b) {
        //TODO: implement me?
        return null;
    }

    public static Map<String, String> loadFileMap(String filename) {
        //TODO: implement me?
//        YamlConfig yamlConfig = new YamlConfig(file);
//        for (String otherFileKey : yamlConfig.getMap().keySet()) {
//            kv.put(otherFileKey, (String) yamlConfig.get(otherFileKey));
//        }
        return null;
    }

    public Map<String, String> getKeyValueMap(String key) {
        try {
            if (!map.containsKey(key)) {
                throw new RuntimeException(String.format("Element '%s' could not be found in the yaml", key));
            }

            Map<String, Object> child = (Map<String, Object>) map.get(key);

            //convert other keys in the child to the string, string map
            Map<String, String> kv = YamlConfig.stripOutImportFilesFromMap(child);

            //for each file, load the other files in, assuming they are flat.
            if (child.containsKey("import_files")) {
                List<String> listOfFiles = getList("import_files", child);

                //For each file get the map and merge it with the current map
                for (String file : listOfFiles) {
                    Map<String, String> fileMap = YamlConfig.loadFileMap(file);
                    kv = mergeMap(fileMap, kv);
                }
            }

            return kv;
        } catch (ClassCastException e) {
            throw new RuntimeException(String.format("Element '%s' is not a map.", key), e);
        }
    }

    public File getFile(String key) {
        return new File(pathToConfiguration.concat("/" + get(key)));
    }

    public Boolean exists(String key) {
        return map.containsKey(key);
    }
}