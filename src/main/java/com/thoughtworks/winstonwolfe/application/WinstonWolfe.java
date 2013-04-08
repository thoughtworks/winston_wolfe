package com.thoughtworks.winstonwolfe.application;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.config.YamlConfigLoader;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.DataSourceFactory;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.NamedEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ReportingEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ScriptEndPointFactory;
import com.thoughtworks.winstonwolfe.reporting.HtmlReport;
import com.thoughtworks.winstonwolfe.runner.CommandLineArguments;
import com.thoughtworks.winstonwolfe.script.Script;
import com.thoughtworks.winstonwolfe.validators.ReportingValidatorFactory;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;

public class WinstonWolfe {
    public static void main(final String[] args) throws Exception {
        CommandLineArguments arguments = new CommandLineArguments(args);

        WinstonConfig endpointConfig = new YamlConfigLoader().load(arguments.getPathToConfiguration());
        WinstonConfig scriptConfig = new YamlConfigLoader().load(arguments.getPathToTestScript());

        HtmlReport report = new HtmlReport();

        NamedEndPointFactory namedEndPointFactory = new NamedEndPointFactory(endpointConfig);
        ScriptEndPointFactory scriptEndPointFactory = new ScriptEndPointFactory(scriptConfig, namedEndPointFactory);
        ReportingEndPointFactory reportingEndPointFactory = new ReportingEndPointFactory(report, scriptEndPointFactory);

        DataSourceFactory dataSourceFactory = new DataSourceFactory(scriptConfig);
        DataSource requestDataSource = dataSourceFactory.buildDataSource();

        ResponseValidatorFactory validatorFactory = new ResponseValidatorFactory(scriptConfig);
        ReportingValidatorFactory reportingValidatorFactory = new ReportingValidatorFactory(report,validatorFactory);

        Script script = new Script(reportingEndPointFactory, requestDataSource, reportingValidatorFactory);
        try {
            script.run();
        } finally {
            System.out.println(report.render());
        }
    }

}