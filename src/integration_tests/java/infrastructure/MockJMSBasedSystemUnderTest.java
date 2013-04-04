package infrastructure;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.jetty.server.Server;

import javax.jms.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class MockJMSBasedSystemUnderTest implements MessageListener {

    private String cannedResponse = "";
    private String lastRequest = "No requests received yet.";
    private Destination requestQueue;
    private Destination reponseQueue;
    private Session session;
    private Connection conn;

    public MockJMSBasedSystemUnderTest(String cannedResponse) {
        this.cannedResponse = cannedResponse;
    }

    public void startServer() throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        conn = connectionFactory.createConnection();
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        requestQueue = session.createQueue("requestQueue");
        reponseQueue = session.createQueue("responseQueue");

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

            MessageProducer producer = session.createProducer(reponseQueue);
            BytesMessage responseMessage = session.createBytesMessage();
            responseMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
            responseMessage.writeBytes(cannedResponse.getBytes("UTF-8"));
            producer.send(responseMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}