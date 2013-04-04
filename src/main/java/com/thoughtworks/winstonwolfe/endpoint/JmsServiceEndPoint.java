package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.StringDataSource;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Scanner;

public class JmsServiceEndPoint implements ServiceEndPoint {

    private WinstonConfig config;

    public JmsServiceEndPoint(WinstonConfig endpointConfig) {
        config = endpointConfig;
    }

    @Override
    public DataSource send(DataSource data) throws Exception {
        Properties env = getProperties();

        InitialContext context = new InitialContext(env);
        Connection conn = getConnection(context);
        try {
            conn.start();

            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination request = (Destination) context.lookup(config.getString("request_queue"));
            Destination response = (Destination) context.lookup(config.getString("response_queue"));


            MessageProducer producer = session.createProducer(request);
            BytesMessage requestMessage = sendMessage(data, session, producer);


            MessageConsumer consumer = getMessageConsumerUsingCorrelationId(session, response, requestMessage);
            return getResponse(consumer);
        } finally {
            conn.close();
        }
    }

    private DataSource getResponse(MessageConsumer consumer) throws JMSException, UnsupportedEncodingException {
        BytesMessage responseMessage = (BytesMessage) consumer.receive(2000);
        if (responseMessage == null) {
            throw new RuntimeException("received null response message - probably due to timeout");
        }

        byte[] bytes = new byte[(int) responseMessage.getBodyLength()];
        responseMessage.readBytes(bytes);

        return new StringDataSource(new String(bytes, config.getString("encoding")));
    }

    private MessageConsumer getMessageConsumerUsingCorrelationId(Session session, Destination response, BytesMessage requestMessage) throws JMSException {
        String selector = String.format("JMSCorrelationID = '%s'", requestMessage.getJMSMessageID());
        return session.createConsumer(response, selector);
    }

    private BytesMessage sendMessage(DataSource data, Session session, MessageProducer producer) throws JMSException, UnsupportedEncodingException {
        BytesMessage requestMessage = session.createBytesMessage();
        requestMessage.writeBytes(data.getData().getBytes(config.getString("encoding")));
        producer.send(requestMessage);
        return requestMessage;
    }

    private Connection getConnection(InitialContext context) throws NamingException, JMSException {
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(config.getString("connection_factory_name"));
        return connectionFactory.createConnection(config.getString("jms_username"), config.getString("jms_password"));
    }

    private Properties getProperties() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, config.getString("context_factory"));
        env.put(Context.PROVIDER_URL, config.getString("provider_url"));
        env.put(Context.SECURITY_PRINCIPAL, config.getString("jndi_username"));
        env.put(Context.SECURITY_CREDENTIALS, config.getString("jndi_password"));
        return env;
    }


}