package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.StringDataSource;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

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

            Destination requestQueue = getRequestQueue(context, session);
            Destination responseQueue = getResponseQueue(context, session);


            MessageProducer producer = session.createProducer(requestQueue);
            BytesMessage requestMessage = sendMessage(data, session, producer, responseQueue);


            MessageConsumer consumer = getMessageConsumerUsingCorrelationId(session, responseQueue, requestMessage);
            return getResponse(consumer);
        } finally {
            conn.close();
        }
    }

    private Destination getRequestQueue(InitialContext context, Session session) throws JMSException, NamingException {
        if (config.exists("request_queue_type")) {
            if (config.getString("request_queue_type") == "dynamic") {
                return session.createTemporaryQueue();
            }
        }

        return (Destination) context.lookup(config.getString("request_queue"));
    }

    private Destination getResponseQueue(InitialContext context, Session session) throws JMSException, NamingException {
        if (config.exists("response_queue_type")) {
            if (config.getString("response_queue_type") == "dynamic") {
                return session.createTemporaryQueue();
            }
        }

        return (Destination) context.lookup(config.getString("response_queue"));
    }

    private DataSource getResponse(MessageConsumer consumer) throws JMSException, UnsupportedEncodingException {
        BytesMessage responseMessage = (BytesMessage) consumer.receive(getTimeout());
        if (responseMessage == null) {
            throw new RuntimeException("received null response message - probably due to timeout");
        }

        byte[] bytes = new byte[(int) responseMessage.getBodyLength()];
        responseMessage.readBytes(bytes);

        return new StringDataSource(new String(bytes, config.getString("encoding")));
    }

    private Integer getTimeout() {
        return config.exists("timeout") ? config.getInt("timeout") : 5000;
    }

    private MessageConsumer getMessageConsumerUsingCorrelationId(Session session, Destination response, BytesMessage requestMessage) throws JMSException {
        String selector = String.format("JMSCorrelationID = '%s'", requestMessage.getJMSMessageID());
        return session.createConsumer(response, selector);
    }

    private BytesMessage sendMessage(DataSource data, Session session, MessageProducer producer, Destination responseQueue) throws JMSException, UnsupportedEncodingException {
        BytesMessage requestMessage = session.createBytesMessage();

        requestMessage.setJMSReplyTo(responseQueue);
        setAdditionalProperties(requestMessage);

        requestMessage.writeBytes(data.getData().getBytes(config.getString("encoding")));
        producer.send(requestMessage);
        return requestMessage;
    }

    private void setAdditionalProperties(BytesMessage requestMessage) throws JMSException {
        Map<String, String> properties = config.getSubConfig("additional_properties").getFlatStringMap();
        for (String key : properties.keySet()) {
            if (key.startsWith("SOAPJMS_")) {
                requestMessage.setStringProperty(key, properties.get(key));
            }
        }
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