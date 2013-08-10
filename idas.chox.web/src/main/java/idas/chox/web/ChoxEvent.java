package idas.chox.web;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class ChoxEvent {

    private static final Logger LOG = LoggerFactory.getLogger(ChoxEvent.class);
    Destination destination;
    Session session;

    public ChoxEvent() throws NamingException, JMSException {
        InitialContext initCtx;
        try {
            initCtx = new InitialContext();
        } catch (NamingException ex) {
            LOG.error("Error creating context: {}", ex.getMessage(), ex);
            throw ex;
        }
        Context envContext = (Context) initCtx.lookup("java:comp/env");

        ConnectionFactory connectionFactory = (ConnectionFactory) envContext.lookup("jms/ConnectionFactory");
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
        } catch (JMSException ex) {
            LOG.error("Error creating connection: {}", ex.getMessage(), ex);
            throw ex;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue("jms/queue/foo");
    }

    public void send(String message) throws JMSException {

        MessageProducer producer;
        try {
            producer = session.createProducer(destination);
        } catch (JMSException ex) {
            LOG.error("Error creating producer: {}", ex.getMessage(), ex);
            throw ex;
        }
        TextMessage msg;
        try {
            msg = session.createTextMessage();
        } catch (JMSException ex) {
            LOG.error("Error creating TextMessage: {}", ex.getMessage(), ex);
            throw ex;
        }
        try {
            msg.setText(message);
        } catch (JMSException ex) {
            LOG.error("Error setting message text: {}", ex.getMessage(), ex);
            throw ex;
        }
        try {
            producer.send(msg);
        } catch (JMSException ex) {
            LOG.error("Error sending message: {}", ex.getMessage(), ex);
            throw ex;
        }

    }
}
