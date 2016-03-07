package idas.chox.emailnotification.notifier;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.util.EmailHelper;

public class InvoiceContestedNotifierTest extends AbstractNotifierTest {

    private @Autowired InvoiceContestedNotifier notifier;

    public InvoiceContestedNotifierTest() throws Exception {
        super();
    }

    @Test
    public void checkNotifierWiring() {
        assertNotNull(notifier);
    }

    @Test
    public void testNotifier() throws Exception {
        EmailHelper mockEmailHelper = createMock(EmailHelper.class);
        notifier.setEmailHelper(mockEmailHelper);
        
        // Suppress to prevent spam
        // mockEmailHelper.postMail(isA(Transport.class), isA(String.class), isA(String.class), (String[]) anyObject());
        // expectLastCall().atLeastOnce();

        replay(mockEmailHelper);

        notifier.getAndProcessNotificationData(getSettings(), defaultFrom, defaultTo, false);

        verify(mockEmailHelper);
    }

    @Override
    public NotificationSettingsBean getSettings() {
        NotificationSettingsBean settings = new NotificationSettingsBean("6:1123:ACKNOWLEDGED:test@valexa.com");
        return settings;
    }

}
