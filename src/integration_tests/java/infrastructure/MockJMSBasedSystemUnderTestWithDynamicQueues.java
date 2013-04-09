package infrastructure;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MockJMSBasedSystemUnderTestWithDynamicQueues implements MessageListener {
    private String cannedResponse = "";
    private String lastRequest = "No requests received yet.";
    private Destination requestQueue;
    private Session session;
    private Connection conn;

    public MockJMSBasedSystemUnderTestWithDynamicQueues(String cannedResponse) {
        this.cannedResponse = cannedResponse;
    }

    public void startServer() throws Exception {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "vm://localhost?broker.persistent=false");
        InitialContext context = new InitialContext(env);

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        conn = connectionFactory.createConnection();
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        requestQueue = (Destination) context.lookup("dynamicQueues/requestQueue");

        MessageConsumer consumer = session.createConsumer(requestQueue);
        consumer.setMessageListener(this);
    }

    public void stopServer() throws JMSException {
        conn.close();
    }

    @Override
    public void onMessage(Message message) {
        BytesMessage requestMessage = (BytesMessage) message;
        try {
            byte[] bytes = new byte[(int) requestMessage.getBodyLength()];
            requestMessage.readBytes(bytes);
            lastRequest = new String(bytes, "UTF-8");

            MessageProducer producer = session.createProducer(requestMessage.getJMSReplyTo());

            producer.send(buildResponseMessage(requestMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BytesMessage buildResponseMessage(BytesMessage requestMessage) throws JMSException, UnsupportedEncodingException {
        BytesMessage responseMessage = session.createBytesMessage();
        responseMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
        responseMessage.writeBytes(cannedResponse.getBytes("UTF-8"));
        return responseMessage;
    }
}