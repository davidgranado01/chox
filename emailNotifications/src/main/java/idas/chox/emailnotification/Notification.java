package idas.chox.emailnotification;

import idas.chox.emailnotification.config.EmailNotificationConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;

@PropertySource("classpath:/application.properties")
public class Notification {

    private @Autowired EmailNotificationController emailNotificationController;

    public Notification() {
        @SuppressWarnings("resource")
        ApplicationContext context = new AnnotationConfigApplicationContext();
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(EmailNotificationConfig.class);

        EmailNotificationController notificationController = (EmailNotificationController) context.getBean("notificationController");
        notificationController.start();

        context.close();
    }


}
