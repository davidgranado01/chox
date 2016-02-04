package idas.chox.emailnotification;

import idas.chox.emailnotification.config.EmailNotificationConfig;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmailNotificationConfig.class)
@TestPropertySource("classpath:/application.test.properties")
public abstract class AbstractEmailNotificationTest {

}
