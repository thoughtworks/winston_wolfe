package com.thoughtworks.winstonwolfe.application;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.config.YamlConfigLoader;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.NamedEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ScriptEndPointFactory;
import com.thoughtworks.winstonwolfe.runner.CommandLineArguments;
import com.thoughtworks.winstonwolfe.script.Script;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;

public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);

        WinstonConfig endpointConfig = new YamlConfigLoader().load(arguments.getPathToConfiguration());
        WinstonConfig scriptConfig = new YamlConfigLoader().load(arguments.getPathToTestScript());

        NamedEndPointFactory namedEndPointFactory = new NamedEndPointFactory(endpointConfig);
        ScriptEndPointFactory scriptEndPointFactory = new ScriptEndPointFactory(scriptConfig, namedEndPointFactory);

        FileDataSource requestDataSource = new FileDataSource("read", scriptConfig);

        ResponseValidatorFactory factory = new ResponseValidatorFactory(scriptConfig);

        Script script = new Script(scriptEndPointFactory, requestDataSource, factory);
        script.run();
    }

}