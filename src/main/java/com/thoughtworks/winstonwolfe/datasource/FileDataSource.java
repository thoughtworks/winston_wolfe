package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.YamlConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileDataSource {

    private String key;
    private String filename;

    public FileDataSource(String key, YamlConfig config) {
        this.key = key;
        this.filename = config.get(key);
    }

    public String getData() {
        try {
            return new Scanner(new File(filename)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Unable to find %s file named %s", key, filename), e);
        }
    }
}