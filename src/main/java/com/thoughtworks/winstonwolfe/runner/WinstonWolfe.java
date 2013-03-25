package com.thoughtworks.winstonwolfe.runner;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.request.Request;


public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);
        YamlConfig endpointConfig = new YamlConfig(arguments.getPathToConfiguration());

        EndPointFactory endPointFactory = new EndPointFactory(endpointConfig);

        YamlConfig scriptConfig = new YamlConfig(arguments.getPathToTestScript());
        FileDataSource requestDataSource = new FileDataSource("request", scriptConfig);

        Request request = new Request(requestDataSource);

        //

        request.sendTo(endPointFactory.buildEndPoint());


    }
}