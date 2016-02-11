package idas.chox.emailnotification;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import idas.chox.emailnotification.config.NotificationSettingsBean;
import idas.chox.emailnotification.config.NotificationType;
import idas.chox.emailnotification.notifier.Notifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testLoadSettings() {
        List<NotificationSettingsBean> settings = controller.loadSettingsFile(SETTINGS_FILE_LOCATION);
        assertNotNull(settings);
        assertTrue(settings.size() >= 1);

        NotificationSettingsBean bean = settings.get(0);
        assertTrue(bean.getEmailAddressses().size() >= 1);
        assertNotNull(bean.getInsurerId());
        assertNotNull(bean.getChoId());
        assertNotNull(bean.getType());

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
        Map<NotificationType, Notifier> testNotifiers = new HashMap<NotificationType, Notifier>();
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        testNotifiers.put(NotificationType.CLOSED, mockNotifier);

        Date now = new Date();

        controller.setNotifiers(testNotifiers);
        controller.setDateFrom(now);
        controller.setDateTo(now);

        mockNotifier.getAndProcessNotificationData(isA(NotificationSettingsBean.class), isA(Date.class), isA(Date.class), isA(Boolean.class));
        expectLastCall();
        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:CLOSED:test@valexa.com"), true);

        verify(mockNotifier);

    }

    @Test
    public void testProcessFail() {
        // Note that the error is recorded in the logs, but an exception is not thrown.

        Map<NotificationType, Notifier> testNotifiers = new HashMap<NotificationType, Notifier>();
        Notifier mockNotifier = EasyMock.createMock(Notifier.class);
        testNotifiers.put(NotificationType.CLOSED, mockNotifier);

        Date now = new Date();

        controller.setNotifiers(testNotifiers);
        controller.setDateFrom(now);
        controller.setDateTo(now);

        replay(mockNotifier);

        controller.process(new NotificationSettingsBean("6:1123:QUANTUMED:test@valexa.com"), true);

        verify(mockNotifier);

    }

    @Test
    public void testObtainDateTo() {
         Date now = new Date();  
         Date  toDate = controller.obtainDateTo();
        assertFalse(toDate.before(now));
    }

    @Test
    public void testObtainDateFrom() throws ParseException {
        // NOTE this test relies upon the value in lastrun.txt

        SimpleDateFormat dateFormat = new SimpleDateFormat(EmailNotificationController.DATE_FORMAT);
        Date expected = dateFormat.parse("20150701");

        Date fromDate = controller.obtainDateFrom("20150701");
        assertEquals(expected, fromDate);
    }
}
