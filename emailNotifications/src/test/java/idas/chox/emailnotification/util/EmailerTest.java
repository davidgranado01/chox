package idas.chox.emailnotification.util;

import static org.junit.Assert.assertNotNull;
import idas.chox.emailnotification.AbstractEmailNotificationTest;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Transport;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailerTest extends AbstractEmailNotificationTest {

    private @Autowired EmailHelper emailHelper;
    private static final String[] recipients = { "mark.allen@valexa.com" };

    @Test
    public void checkWiring() {
        assertNotNull(emailHelper);
    }

    @Ignore
    @Test
    public void testSendSingleEmail() throws UnsupportedEncodingException, MessagingException {
        Transport transport = emailHelper.getTransport();
        emailHelper.postMail(transport, "Test", "TestMesage", recipients);
        emailHelper.closeTransport(transport);
    }

    @Ignore
    @Test
    public void testSendBulkEmail() throws UnsupportedEncodingException, MessagingException {
        Transport transport = emailHelper.getTransport();
        for (int i = 0; i < 200; i++) {
            emailHelper.postMail(transport, "Test", "TestMesage", recipients);
            sleep20Seconds();
        }
        emailHelper.closeTransport(transport);
    }

    private void sleep20Seconds() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
