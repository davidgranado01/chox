package idas.chox.emailnotification;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.config.NotificationType;
import idas.chox.emailnotification.notifier.Notifier;

public class EmailNotificationControllerTest extends AbstractEmailNotificationTest {

    private @Autowired EmailNotificationController controller;

    // Enable FULL test if required. Otherwise, suppress to prevent spam
    @Ignore
    @Test
    public void testController() {

        // Run the core process
        String[] mainParameters = { "-limit1", "-sendEmails" };
        Notification.main(mainParameters);
    }

    @Ignore
    @Test
    public void testControllerInvalidParams() {

        // Run the core process
        String[] mainParameters = { "-invalid" };
        Notification.main(mainParameters);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRegisterNotifiers() {
        Map<NotificationType, Notifier> mockNotifiers = EasyMock.createMock(Map.class);
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        
        controller.setNotifiers(mockNotifiers);

        expect(mockNotifiers.put(isA(NotificationType.class), isA(Notifier.class))).andReturn(mockNotifier).times(6);
        replay(mockNotifiers);
        
        controller.registerNotifiers(false);
        
        verify(mockNotifiers);

    }

    @Test
    public void testProcessPass() {
        Map<NotificationType, Notifier> testNotifiers = new HashMap<>();
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        testNotifiers.put(NotificationType.CLOSED, mockNotifier);

        controller.setNotifiers(testNotifiers);

        expect(mockNotifier.getAndProcessNotificationData(isA(NotificationSettingsBean.class), isA(String.class), isA(String.class), isA(Boolean.class))).andReturn(1);
        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:CLOSED:test@valexa.com"), "2015-01-01", "2015-01-08", true);
        verify(mockNotifier);

    }

    @Test
    public void testProcessFail() {
        // Note that the error is recorded in the logs, but an exception is not thrown.

        Map<NotificationType, Notifier> testNotifiers = new HashMap<>();
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        testNotifiers.put(NotificationType.CLOSED, mockNotifier);

        controller.setNotifiers(testNotifiers);

        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:QUANTUMED:test@valexa.com"), "2015-01-01", "2015-01-08", true);


        verify(mockNotifier);

    }

}
