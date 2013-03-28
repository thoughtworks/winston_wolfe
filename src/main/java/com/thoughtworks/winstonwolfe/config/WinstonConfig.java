package com.thoughtworks.winstonwolfe.config;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface WinstonConfig {

    String getString(String key);

    List<String> getList(String key);

    WinstonConfig getSubConfig(String key);

    File getFile(String key);

    Boolean exists(String key);

    Map<String, Object> getMap();
    Map<String,String> getFlatStringMap();
}