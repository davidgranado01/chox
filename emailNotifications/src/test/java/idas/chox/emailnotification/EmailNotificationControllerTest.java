package idas.chox.emailnotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.config.NotificationType;
import idas.chox.emailnotification.notifier.Notifier;

public class EmailNotificationControllerTest extends AbstractEmailNotificationTest {

    private static final String SETTINGS_FILE_LOCATION = "./src/test/resources/settings.txt";

    private @Autowired EmailNotificationController controller;

    // Enable FULL test if required. Otherwise, suppress to prevent spam
    // @Ignore
    @Test
    public void testController() {

        // Limit each notifier to a single email (spam reduction)
        Notification.setLIMIT1(true);

        // Run the core process
        String[] mainParameters = { SETTINGS_FILE_LOCATION, "20151125", "true" };
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

        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        controller.setNotifiers(testNotifiers);

        mockNotifier.getAndProcessNotificationData(isA(NotificationSettingsBean.class), now, now, isA(Boolean.class));
        expectLastCall();
        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:CLOSED:test@valexa.com"), now, now, true);

        verify(mockNotifier);

    }

    @Test
    public void testProcessFail() {
        // Note that the error is recorded in the logs, but an exception is not thrown.

        Map<NotificationType, Notifier> testNotifiers = new HashMap<>();
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        testNotifiers.put(NotificationType.CLOSED, mockNotifier);

        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        controller.setNotifiers(testNotifiers);

        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:QUANTUMED:test@valexa.com"), now, now, true);

        verify(mockNotifier);

    }

}
