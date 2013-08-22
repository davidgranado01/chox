package idas.chox.jmsClient;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import idas.chox.events.Event;

public class App implements MessageListener {

    private static Logger LOG = LoggerFactory.getLogger(App.class);
    private static String queueName;
    public static String brokerURL = "tcp://localhost:61616";
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private static void printUsageAndExit() {
        System.out.println("Usage: java -jar jmsClient-1.0-jar-with-dependencies.jar <queueName>");
        System.exit(-1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsageAndExit();
        }
        queueName = args[0];
        LOG.info("Running JMS Client for queue '{}'", queueName);
        App app = new App();
        LOG.debug("Running.....");
        app.run();
        LOG.debug("Finished.");
    }

    public void run() {
        try {
            factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start();
            LOG.debug("Connection started.");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            consumer = session.createConsumer(destination);
            LOG.debug("Consumer created.");
            consumer.setMessageListener(this);
            LOG.debug("Listener set.");
        } catch (Exception e) {
            LOG.error("Caught Exception: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(Message message) {
        LOG.debug("on/message fired!!");
        try {
            if (message instanceof ActiveMQTextMessage) {
                ActiveMQTextMessage txtMessage = (ActiveMQTextMessage) message;
                String jsonString = txtMessage.getText();
                LOG.debug("Text Message received: {}", jsonString);
                // Do Something
                Gson gson = new Gson();
                Event ev = gson.fromJson(jsonString, Event.class);
                LOG.info("Received {}", ev);
                txtMessage.acknowledge();
            }  else {
                LOG.error("Invalid message type received: {}", message.getClass());
            }
        } catch (JMSException e) {
            LOG.error("Exception Caught: {}", e.getMessage(), e);
        }
    }
}