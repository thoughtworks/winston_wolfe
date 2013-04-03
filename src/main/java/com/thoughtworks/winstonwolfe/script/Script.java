package com.thoughtworks.winstonwolfe.script;

import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.endpoint.EndPointFactory;
import com.thoughtworks.winstonwolfe.endpoint.ServiceEndPoint;
import com.thoughtworks.winstonwolfe.validators.ValidatorFactory;

import java.io.IOException;

public class Script {
    private EndPointFactory endPointFactory;
    private DataSource requestDataSource;
    private ValidatorFactory factory;

    public Script(EndPointFactory endPointFactory, DataSource requestDataSource, ValidatorFactory factory) {
        this.endPointFactory = endPointFactory;
        this.requestDataSource = requestDataSource;
        this.factory = factory;
    }

    public void run() throws IOException {
        ServiceEndPoint endPoint = endPointFactory.buildEndPoint();
        DataSource actualResponseDataSource = endPoint.send(requestDataSource);
        factory.buildValidator().validateAgainst(actualResponseDataSource).assertSuccess();
    }
}