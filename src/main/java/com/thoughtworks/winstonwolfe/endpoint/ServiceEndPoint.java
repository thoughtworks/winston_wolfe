package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.request.Request;
import com.thoughtworks.winstonwolfe.response.Response;

import java.io.IOException;

public interface ServiceEndPoint {
    Response send(String data) throws IOException;
}