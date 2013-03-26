package com.thoughtworks.winstonwolfe.application;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.runner.CommandLineArguments;
import com.thoughtworks.winstonwolfe.validators.ExactMatchValidator;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;

public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);

        YamlConfig endpointConfig = new YamlConfig(arguments.getPathToConfiguration());

        EndPointFactory endPointFactory = new EndPointFactory(endpointConfig);

        YamlConfig scriptConfig = new YamlConfig(arguments.getPathToTestScript());
        FileDataSource requestDataSource = new FileDataSource("request", scriptConfig);




        ServiceEndPoint endPoint = endPointFactory.buildEndPoint();
        DataSource actualResponseDataSource = endPoint.send(requestDataSource);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(scriptConfig);
        factory.buildValidator().validateAgainst(actualResponseDataSource);
    }
}