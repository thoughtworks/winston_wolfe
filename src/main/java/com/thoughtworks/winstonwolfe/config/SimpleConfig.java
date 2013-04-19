package com.thoughtworks.winstonwolfe.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleConfig implements WinstonConfig {
    private Map<String, Object> map;
    protected String basePath;

    public SimpleConfig(Map<String, Object> map, String basePath) {
        this.map = map;
        this.basePath = basePath;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public String getString(String key) {
        return (String) typeSafeGet(key, String.class);
    }

    protected Object typeSafeGet(String key, Class clazz) {
        if (map.containsKey(key)) {
            Object result = map.get(key);
            if (!clazz.isInstance(result)) {
                throw new RuntimeException(buildIncorrectTypeMessage(key, clazz));
            }
            return result;
        } else {
            throw new RuntimeException(String.format("The configuration key '%s' could not be found.", key));
        }

    }

    private String buildIncorrectTypeMessage(String key, Class clazz) {
        return String.format("The configuration value for '%s' is not a %s.", key, clazz.getSimpleName());
    }

    @Override
    public List<String> getList(String key) {
        return (List<String>) typeSafeGet(key, List.class);
    }


    @Override
    public WinstonConfig getSubConfig(String key) {
        return new SimpleConfig((Map<String, Object>) typeSafeGet(key, Map.class), basePath);
    }

    @Override
    public File getFile(String key){
        return new File(basePath + "/" + getString(key));
    }

    @Override
    public Boolean exists(String key) {
        return map.containsKey(key);
    }

    @Override
    public Map<String, String> getFlatStringMap() {
        Map<String, String> results = new HashMap<String,String>();
        for (String key : map.keySet()) {
            results.put(key, getString(key));
        }
        return results;
    }

    @Override
    public Integer getInt(String key) {
        return (Integer) typeSafeGet(key, Integer.class);
    }

    @Override
    public boolean isSimpleConfig(String key) {
        try {
            getSubConfig(key);
        } catch (RuntimeException e) {
            if (e.getMessage().equals(buildIncorrectTypeMessage(key, Map.class))) {
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}