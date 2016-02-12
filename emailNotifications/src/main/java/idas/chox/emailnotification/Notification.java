package idas.chox.emailnotification;

import idas.chox.emailnotification.config.EmailNotificationConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;

@PropertySource("classpath:/application.properties")
public class Notification {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private @Autowired EmailNotificationController emailNotificationController;


    public Notification() {
        @SuppressWarnings("resource")
        ApplicationContext context = new AnnotationConfigApplicationContext();
        AutowireCapableBeanFactory acbFactory = context.getAutowireCapableBeanFactory();
        acbFactory.autowireBean(this);
    }

    public static void main(String[] args) {
        String startDate = null;
        String settingsOverideFile = null;
        boolean enableEmails = false;
        boolean limit1 = false;
        
        // check command-line arguments
        for (int i= 0; i < args.length; i++) {
            switch(args[i]) {
                case "-sendEmails":
                    enableEmails = true;
                    break;
                case "-startDate":
                    if (i+1 >= args.length) {
                        showUsageAndExit();
                    }
                    startDate = args[++i];
                    // Check its a date in the correct format
                    SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
                    try {
                        df.parse(startDate);
                    } catch (ParseException e) {
                        showUsageAndExit();
                    }
                    break; 
                case "-settings":
                    if (i+1 >= args.length) {
                        showUsageAndExit();
                    }
                    settingsOverideFile = args[++i];
                    if (!isFileReadable(settingsOverideFile)) {
                        showUsageAndExit();
                    }
                    break; 
            case "-limit1":
                limit1 = true;
                break; 
                default:
                    showUsageAndExit();
                    break;
            }
        }
        
        if (startDate == null) {
            startDate = getYesterdayDateAsString();
        }

        try (AbstractApplicationContext context = new AnnotationConfigApplicationContext(EmailNotificationConfig.class)) {
            
            EmailNotificationController notificationController = (EmailNotificationController) context.getBean("notificationController");

            notificationController.start(startDate, new SimpleDateFormat(DATE_FORMAT).format(new Date()), enableEmails, limit1, settingsOverideFile);

        }
    }



    protected static void showUsageAndExit() {
        System.out.println("Usage: java -jar <jarfilename> [-sendEmails] [-startDate yyyy-mm-dd] [-settings <settingsFile>]"); // NOSONAR
        System.exit(-1);
    }
    
    private static String getYesterdayDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
        return dateFormat.format(cal.getTime());
    }
    
    private static boolean isFileReadable(String filename) {
        boolean result = false;
        
        Path file = Paths.get(filename);
        
        if (Files.isRegularFile(file) && Files.isReadable(file)) {
            return true;
        }
        return result;
    }
}
