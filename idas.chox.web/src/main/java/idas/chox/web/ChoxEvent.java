package idas.chox.web;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 *
 * @author John
 */
public class ChoxEvent {
    private static final Logger LOG = LoggerFactory.getLogger(ChoxEvent.class);
    @Autowired
    protected JmsTemplate queue1JMSTemplate;
    Destination destination;
    Session session;

    public void setQueue1JMSTemplate(JmsTemplate queue1JMSTemplate) {
        this.queue1JMSTemplate = queue1JMSTemplate;
    }

    
    public ChoxEvent() throws NamingException, JMSException {
/*
        ßInitialContext initCtx;
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
 */
    }

    public void send(final String messageText) {
        LOG.info("Sending JMS message '{}' with template {}", messageText, queue1JMSTemplate);
        try {
            queue1JMSTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    LOG.info("Creating message with session '{}'", session);
                    Message message;
                    try {
                        message = session.createTextMessage(messageText);
                    } catch (JMSException ex) {
                        LOG.error("Error creating message: {}", ex.getMessage());
                        throw ex;
                    }
                    LOG.info("Done creating message");
                    return message;
                }
            });
        } catch (Exception ex) {
            LOG.error("Error sending message: {}", ex.getMessage());
//            throw new JMSException(ex);
        }
    }

    public void sendMessage(final String message) throws JMSException {
        LOG.info("CHOX Event send request received: '{}'", message);

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
        LOG.info("CHOX Event sent ok: '{}'", message);
    }
}
