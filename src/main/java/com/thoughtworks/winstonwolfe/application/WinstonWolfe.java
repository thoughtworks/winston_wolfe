package com.thoughtworks.winstonwolfe.application;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.config.YamlConfigLoader;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.runner.CommandLineArguments;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;

public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);

        WinstonConfig endpointConfig = new YamlConfigLoader().load(arguments.getPathToConfiguration());

        EndPointFactory endPointFactory = new EndPointFactory(endpointConfig);

        WinstonConfig scriptConfig = new YamlConfigLoader().load(arguments.getPathToTestScript());
        FileDataSource requestDataSource = new FileDataSource("request", scriptConfig);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(scriptConfig);

        ServiceEndPoint endPoint = endPointFactory.buildEndPoint(scriptConfig.getString("send_to"));
        DataSource actualResponseDataSource = endPoint.send(requestDataSource);
        factory.buildValidator().validateAgainst(actualResponseDataSource);
    }
}