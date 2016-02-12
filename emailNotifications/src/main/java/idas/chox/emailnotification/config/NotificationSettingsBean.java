package idas.chox.emailnotification.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Simple bean to hold the Email Notification Settings.
 */
@Component
public class NotificationSettingsBean {

    private static final String DELIMITER = ":";
    private static final String EMAIL_DELIMITER = ",";
    
    private Integer insurerId;
    private Integer choId;
    private NotificationType type;
    private List<String> emailAddressses;
    
    public NotificationSettingsBean() {
    }

    public NotificationSettingsBean(String settingsString) {
        // Split settingsString into settings fields
        String[] settings = settingsString.split(DELIMITER);

        insurerId = Integer.parseInt(settings[0]);
        choId = Integer.parseInt(settings[1]);
        type = NotificationType.valueOf(settings[2]);
        
        String[] emailArray = settings[3].split(EMAIL_DELIMITER);
        emailAddressses = Arrays.asList(emailArray);
    }


    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getChoId() {
        return choId;
    }

    public void setChoId(Integer choId) {
        this.choId = choId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public List<String> getEmailAddressses() {
        return emailAddressses;
    }

    public void setEmailAddressses(List<String> emailAddressses) {
        this.emailAddressses = emailAddressses;
    }

}
