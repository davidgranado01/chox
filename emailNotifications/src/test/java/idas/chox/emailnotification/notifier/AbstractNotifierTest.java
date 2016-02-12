package idas.chox.emailnotification.notifier;


import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.emailnotification.util.EmailHelper;
import idas.chox.emailnotification.AbstractEmailNotificationTest;
import idas.chox.emailnotification.config.NotificationSettingsBean;

public abstract class AbstractNotifierTest extends AbstractEmailNotificationTest {

    private @Autowired EmailHelper autowiredEmailHelper;

    protected static final String DATE_FORMAT = "yyyyMMdd";
    protected String defaultFrom = "2015-01-01";
    protected String defaultTo = "2015-01-08";

    public abstract void testNotifier() throws Exception;

    public abstract NotificationSettingsBean getSettings();

    public AbstractNotifierTest() throws Exception {
    }

    @Test
    public void checkWiring() {
        assertNotNull(autowiredEmailHelper);
    }

}
