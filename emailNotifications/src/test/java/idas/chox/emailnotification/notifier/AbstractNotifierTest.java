package idas.chox.emailnotification.notifier;

import static org.junit.Assert.assertNotNull;
import idas.chox.core.util.EmailHelper;
import idas.chox.emailnotification.AbstractEmailNotificationTest;
import idas.chox.emailnotification.config.NotificationSettingsBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractNotifierTest extends AbstractEmailNotificationTest {

    private @Autowired EmailHelper autowiredEmailHelper;

    protected static final String DATE_FORMAT = "yyyyMMdd";
    protected Date defaultFrom;
    protected Date defaultTo;

    public abstract void testNotifier() throws Exception;

    public abstract NotificationSettingsBean getSettings();

    public AbstractNotifierTest() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        defaultFrom = dateFormat.parse("20150101");
        defaultTo = dateFormat.parse("20150108");
    }

    @Test
    public void checkWiring() {
        assertNotNull(autowiredEmailHelper);
    }

}
