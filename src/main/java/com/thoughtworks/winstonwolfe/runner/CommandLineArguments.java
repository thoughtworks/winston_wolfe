package com.thoughtworks.winstonwolfe.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineArguments {
    private String pathToConfiguration;
    private String pathToTestScript;

    public CommandLineArguments(String[] args) {
        List<String> arguments = new ArrayList<String>(Arrays.asList(args));

        pathToConfiguration = arguments.get(0);
        pathToTestScript = arguments.get(1);
    }

    public String getPathToConfiguration() {
        return pathToConfiguration;
    }

    public String getPathToTestScript() {
        return pathToTestScript;
    }
}