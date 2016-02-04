package idas.chox.emailnotification;

import static org.junit.Assert.assertNotNull;
import idas.chox.core.util.EmailHelper;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailTest extends AbstractEmailNotificationTest {

    private @Autowired EmailHelper emailHelper;

    @Test
    public void checkWiring() {
        assertNotNull(emailHelper);
    }

    // Enable test if required. Otherwise, suppress to prevent spam
    @Ignore
    @Test
    public void checkSend() throws UnsupportedEncodingException, MessagingException {
        String[] recipients = { "mark.allen@valexa.com" };
        emailHelper.postMail("Test email", "Test email", recipients);
    }

}
