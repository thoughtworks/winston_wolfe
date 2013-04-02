package com.thoughtworks.winstonwolfe.script;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.FileDataSource;
import com.thoughtworks.winstonwolfe.endpoint.ScriptEndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.validators.ResponseValidatorFactory;

import java.io.IOException;

public class Script {
    private ScriptEndPointFactory endPointFactory;
    private DataSource requestDataSource;
    private ResponseValidatorFactory factory;

    public Script(ScriptEndPointFactory endPointFactory, DataSource requestDataSource, ResponseValidatorFactory factory) {
        this.endPointFactory = endPointFactory;
        this.requestDataSource = requestDataSource;
        this.factory = factory;
    }

    public void run() throws IOException {
        ServiceEndPoint endPoint = endPointFactory.buildEndPoint();
        DataSource actualResponseDataSource = endPoint.send(requestDataSource);
        factory.buildValidator().validateAgainst(actualResponseDataSource);
    }
}