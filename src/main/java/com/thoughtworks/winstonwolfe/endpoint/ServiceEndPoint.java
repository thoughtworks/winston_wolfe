package com.thoughtworks.winstonwolfe.endpoint;

import java.io.IOException;

public interface ServiceEndPoint {
    void send(String data) throws IOException;
}