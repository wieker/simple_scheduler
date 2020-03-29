package org.allesoft.enterprise_db;

import org.springframework.jms.core.JmsTemplate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class SpringConsumer extends ConsumerBean implements MessageListener {
    private JmsTemplate template;
    private String myId = "foo";
    private Destination destination;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public void start() throws JMSException {
        String selector = "next = '" + myId + "'";

        try {
            ConnectionFactory factory = template.getConnectionFactory();
            connection = factory.createConnection();

            // we might be a reusable connection in spring
            // so lets only set the client ID once if its not set
            synchronized (connection) {
                if (connection.getClientID() == null) {
                    connection.setClientID(myId);
                }
            }

            connection.start();

            session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
            consumer = session.createConsumer(destination, selector, false);
            consumer.setMessageListener(this);
        } catch (JMSException ex) {
            throw ex;
        }
    }

    public void stop() throws JMSException {
        if (consumer != null) {
            consumer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public void onMessage(Message message) {
        super.onMessage(message);
        try {
            message.acknowledge();
        } catch (JMSException e) {
        }
    }

    // Properties
    // -------------------------------------------------------------------------
    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public JmsTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }
}
