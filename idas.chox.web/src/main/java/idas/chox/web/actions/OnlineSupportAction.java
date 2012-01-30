package idas.chox.web.actions;

import idas.chox.core.util.DateHelper;
import idas.chox.core.util.EmailHelper;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class OnlineSupportAction extends BaseAction {

    private String iSupplierReference;
    private String iSubject;
    private String iMessage;
    private String iEmail;
    private String iPhone;
    private String actionResult;
    private Properties props;
    private static final String email_date_format = "dd MMMM yyyy";
    private static final String propertiesFile = "/application.properties";

    public String getiEmail() {
        return iEmail;
    }

    public void setiEmail(String iEmail) {
        this.iEmail = iEmail;
    }

    public String getiPhone() {
        return iPhone;
    }

    public void setiPhone(String iPhone) {
        this.iPhone = iPhone;
    }


    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public String getIMessage() {
        return iMessage;
    }

    public void setIMessage(String iMessage) {
        this.iMessage = iMessage;
    }

    public String getISubject() {
        return iSubject;
    }

    public void setISubject(String iSubject) {
        this.iSubject = iSubject;
    }

    public String getISupplierReference() {
        return iSupplierReference;
    }

    public void setISupplierReference(String iSupplierReference) {
        this.iSupplierReference = iSupplierReference;
    }

    public String saveMessage() {

        try {
        	Resource resource = new ClassPathResource(propertiesFile);
        	props = PropertiesLoaderUtils.loadProperties(resource);

            String onlineSupportDefaultEmail = props.getProperty("onlineSupportDefaultEmail");
            String smtpHostName = props.getProperty("smtpHostName");
            String smtpPort = props.getProperty("smtpPort");
            String smtpEmailUser = props.getProperty("smtpEmailUser");
            String smtpEmailUserPassword = props.getProperty("smtpEmailPassword");

            String[] recipients = {onlineSupportDefaultEmail};

            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailUserPassword);
            String emailMessage = doConstructEmailMessage(iSubject, iSupplierReference, iMessage, iEmail, iPhone);
            emailHelper.postMail(iSubject, emailMessage, recipients);
            this.getActionResponse().AssignMessageResult("Your support request has been sent successfully. A member of the CHOX support team will be in touch shortly.");

        } catch (Exception ex) {
            handleException(ex);
            this.getActionResponse().AddError("Please try again.");
        }

        return SUCCESS;
    }

    private String doConstructEmailMessage(String sSubject, String sSupplierReference, String sMessage, String sEmail, String sPhone) {

        StringBuffer emailMsg = new StringBuffer();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By: " + getAuthenticatedUser().getDisplayName());
        emailMsg.append("\n");
        emailMsg.append("Email: " + sEmail);
        emailMsg.append("\n");
        emailMsg.append("Phone Number: " + sPhone);
        emailMsg.append("\n");
        emailMsg.append("Date: " + DateHelper.getCurrentDateWithFormat(email_date_format));
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");
        emailMsg.append("Supplier Reference Number: " + sSupplierReference);
        emailMsg.append("\n");
        emailMsg.append("Subject: " + sSubject);
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");
        emailMsg.append(sMessage);
        emailMsg.append("\n");
        emailMsg.append("======================================================================\n");

        return emailMsg.toString();
    }

    @Override
    public String execute() {
            return SUCCESS;
    }
}
