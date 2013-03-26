package com.thoughtworks.winstonwolfe.datasource;

import com.thoughtworks.winstonwolfe.config.YamlConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileDataSource implements DataSource {

    private String key;
    private File file;

    public FileDataSource(String key, YamlConfig config) {
        this.key = key;
        this.file = config.getFile(key);
    }

    @Override
    public String getData() {
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Unable to find %s file named %s", key, file), e);
        }
    }
}