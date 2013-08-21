package idas.chox.jmsClient;

import java.io.InputStream;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
/*****
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            InputStream verboseConfigFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback-verbose.xml");
            configurator.doConfigure(verboseConfigFile);
        } catch (JoranException je) {
            LOG.error("Error activating verbose messaging: {}", je.getMessage());
            if (je.getCause() != null) {
                LOG.error("Caused by: {}", je.getCause().getMessage());
            }
        }
******/
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
            if (message instanceof TextMessage) {
                TextMessage txtMessage = (TextMessage) message;
                LOG.info("Message received: {}", txtMessage.getText());
                // Do Something
                txtMessage.acknowledge();
            } else {
                LOG.error("Invalid message received.");
            }
        } catch (JMSException e) {
            LOG.error("Exception Caught: {}", e.getMessage(), e);
        }
    }
}