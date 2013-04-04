package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.StringDataSource;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class JmsServiceEndPoint implements ServiceEndPoint {


    private WinstonConfig config;

    public JmsServiceEndPoint(WinstonConfig endpointConfig) {


        config = endpointConfig;
    }

    @Override
    public DataSource send(DataSource data) throws Exception {

        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, config.getString("context_factory"));
        env.put(Context.PROVIDER_URL, config.getString("provider_url"));
        env.put(Context.SECURITY_PRINCIPAL, config.getString("jndi_username"));
        env.put(Context.SECURITY_CREDENTIALS, config.getString("jndi_password"));

        InitialContext context = new InitialContext(env);
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(config.getString("connection_factory_name"));
        Connection conn =  connectionFactory.createConnection(config.getString("jms_username"), config.getString("jms_password"));
        try {
        conn.start();

        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);


        Destination request = (Destination) context.lookup(config.getString("request_queue"));
        Destination response = (Destination) context.lookup(config.getString("response_queue"));

        MessageProducer producer = session.createProducer(request);

        BytesMessage requestMessage = session.createBytesMessage();
        requestMessage.writeBytes(data.getData().getBytes(config.getString("encoding")));
//        requestMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
        producer.send(requestMessage);

        String selector = String.format("JMSCorrelationID = '%s'", requestMessage.getJMSMessageID());
        MessageConsumer consumer = session.createConsumer(response, selector);
        BytesMessage responseMessage = (BytesMessage) consumer.receive(2000);
        if (responseMessage == null) {
         throw new RuntimeException("received null response message - probably due to timeout");
        }
        byte[] bytes = new byte[(int) responseMessage.getBodyLength()];
        responseMessage.readBytes(bytes);
        return new StringDataSource(new String(bytes, config.getString("encoding")));
        } finally {
            conn.close();
        }
    }


}