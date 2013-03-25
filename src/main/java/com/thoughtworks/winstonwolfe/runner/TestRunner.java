package com.thoughtworks.winstonwolfe.runner;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class TestRunner {
    public static final String REQUEST = "request";
    private Map<String, String> config;

    public TestRunner(YamlConfig scriptConfig, EndPointFactory endPointFactory) throws IOException {
        this.config = scriptConfig.getMap();

        if (!config.containsKey(REQUEST)) {
            throw new RuntimeException("Test Script does not contain a 'request' document.");
        }

        getFileContents(config.get(REQUEST));
    }

    public String getFileContents(String filename) throws IOException {
        InputStream fis;
        BufferedReader br;
        String line;
        StringBuilder fileContents = new StringBuilder();

        fis = new FileInputStream(filename);
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        while ((line = br.readLine()) != null) {
            fileContents.append(line);
        }

        return fileContents.toString();
    }
}