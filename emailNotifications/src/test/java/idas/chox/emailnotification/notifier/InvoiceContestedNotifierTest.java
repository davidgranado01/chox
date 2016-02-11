package idas.chox.emailnotification.notifier;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import idas.chox.core.util.EmailHelper;
import idas.chox.emailnotification.config.NotificationSettingsBean;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        
        mockEmailHelper.postMail(isA(String.class), isA(String.class), (String[]) anyObject());
        expectLastCall().atLeastOnce();

        replay(mockEmailHelper);

//        notifier.getAndProcessNotificationData(getSettings(), defaultFrom, defaultTo, true);

        verify(mockEmailHelper);
    }

    @Override
    public NotificationSettingsBean getSettings() {
        NotificationSettingsBean settings = new NotificationSettingsBean("6:1123:ACKNOWLEDGED:test@valexa.com");
        return settings;
    }

}
