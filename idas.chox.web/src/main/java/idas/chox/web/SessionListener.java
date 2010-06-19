package idas.chox.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class SessionListener implements HttpSessionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SessionListener.class);

   // Notification that a new session was created
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        LOG.debug("New session created : {}", session.getId());
        LOG.debug("Session creation time: {}", session.getCreationTime());
    }

    // Notification that a session was invalidated
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();

        LOG.debug("Session destroyed: {}" + session.getId());
    }

}
