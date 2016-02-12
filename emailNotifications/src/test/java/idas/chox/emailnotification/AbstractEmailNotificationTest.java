package idas.chox.emailnotification;

import idas.chox.emailnotification.config.EmailNotificationConfig;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EmailNotificationConfig.class)
@TestPropertySource("classpath:/application.test.properties")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
public abstract class AbstractEmailNotificationTest {

}
