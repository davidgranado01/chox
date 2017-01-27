package idas.chox.jmsClient;

import idas.chox.events.old.Event;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


public class App implements MessageListener {

    private static Logger LOG = LoggerFactory.getLogger(App.class);
    private static String queueName;
    private static Writer writer = null;
//    public static String brokerURL = "failover:(tcp://localhost:61616)";
    public static String brokerURL = "failover:(tcp://localhost:61616)?startupMaxReconnectAttempts=10&maxReconnectAttempts=-1&initialReconnectDelay=500";
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    private static void printUsageAndExit() {
        System.out.println("Usage: java -jar jmsClient.jar [-l] <queueName>");
        System.exit(-1);
    }

    public static void main(String[] args) {
        boolean createLog = false;

        if (args.length  == 1 && !args[0].equals("-l") ) {
            queueName = args[0];
        } else if (args.length  == 2) {
            if (!args[0].equals("-l")) {
                printUsageAndExit();
            }
            createLog = true;
            queueName = args[1];
        } else {
            printUsageAndExit();
        }

        LOG.info("Running JMS Client for queue '{}'", queueName);
        if (createLog) {
            DateFormat df = new SimpleDateFormat("yyyddMM-HH:mm:ss");
            Date today = Calendar.getInstance().getTime();        
            String logFileName = queueName + "-" + df.format(today) + ".log";
            LOG.info("Logging events to file '{}'", logFileName);
            try {
                writer = new OutputStreamWriter(new FileOutputStream(logFileName), "UTF-8");
            } catch (IOException ex) {
                LOG.error("Could not open log file: {}", ex.getMessage(), ex);
                printUsageAndExit();
            }
        }
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
                if (writer != null) {
                    try {
                        writer.write(ev.toString());
                        writer.write("\n");
                        writer.flush();
                    } catch (IOException ex) {
                        LOG.error("Exception writing event to log: {}", ex.getMessage(), ex);
                    }
                }
                txtMessage.acknowledge();
            }  else {
                LOG.error("Invalid message type received: {}", message.getClass());
            }
        } catch (JMSException e) {
            LOG.error("Exception Caught: {}", e.getMessage(), e);
        }
    }
}