package idas.chox.events;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.google.gson.Gson;

/**
 *
 * @author John
 */
public class ChoxJmsEventSender {
    private static final Logger LOG = LoggerFactory.getLogger(ChoxJmsEventSender.class);
    @Autowired
    protected JmsTemplate queue1JMSTemplate;

    public void setQueue1JMSTemplate(JmsTemplate queue1JMSTemplate) {
        this.queue1JMSTemplate = queue1JMSTemplate;
    }

    
    public void send(final Event event) throws Exception {
        // Create a json string from the Event object and send as text message
        Gson gson = new Gson();
        final String jsonMessage = gson.toJson(event);
        LOG.debug("Sending JMS message '{}' with template {}", jsonMessage, queue1JMSTemplate);
        try {
            queue1JMSTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    LOG.debug("Creating message with event: {}\n    session='{}'", event, session);
                    Message message;
                    try {
                        message = session.createTextMessage(jsonMessage);
                        // Set Message Headers, used for routing
                        message.setIntProperty("choId", event.getChoId());
                        message.setIntProperty("insurerId", event.getInsurerId());
                        message.setStringProperty("choxEvent", event.getName());
                        message.setIntProperty("eventId", event.getId());
                        message.setIntProperty("claimType", event.getClaimType());
                        message.setIntProperty("claimId", event.getClaimId());
                    } catch (JMSException ex) {
                        LOG.error("Error creating message: {}", ex.getMessage());
                        throw ex;
                    }
                    LOG.debug("Done creating message");
                    return message;
                }
            });
        } catch (Exception ex) {
            LOG.error("Error sending message: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

}
