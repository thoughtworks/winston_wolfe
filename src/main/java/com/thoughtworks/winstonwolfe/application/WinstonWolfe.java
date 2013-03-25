package com.thoughtworks.winstonwolfe.application;

import com.thoughtworks.winstonwolfe.config.YamlConfig;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.request.Request;
import com.thoughtworks.winstonwolfe.response.Response;
import com.thoughtworks.winstonwolfe.runner.CommandLineArguments;
import com.thoughtworks.winstonwolfe.validators.ExactMatchValidator;

public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);
        YamlConfig endpointConfig = new YamlConfig(arguments.getPathToConfiguration());

        EndPointFactory endPointFactory = new EndPointFactory(endpointConfig);

        YamlConfig scriptConfig = new YamlConfig(arguments.getPathToTestScript());
        FileDataSource requestDataSource = new FileDataSource("request", scriptConfig);
        FileDataSource responseDataSource = new FileDataSource("response", scriptConfig);

        Request request = new Request(requestDataSource);
        Response expectedResponse = new Response(responseDataSource);

        ServiceEndPoint endPoint = endPointFactory.buildEndPoint();

//      request.sendTo(endPoint);
//        response.getFrom(endPoint) ??

        Response actualResponse = endPoint.send(request.getDataSource().getData());

        ExactMatchValidator validator = new ExactMatchValidator();
        validator.validate(actualResponse, expectedResponse);
    }
}