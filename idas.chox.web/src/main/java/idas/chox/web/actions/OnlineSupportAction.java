package idas.chox.web.actions;

import idas.chox.core.util.DateHelper;
import idas.chox.core.util.EmailHelper;
import org.apache.struts2.ServletActionContext;

public class OnlineSupportAction extends BaseAction {

    private String iSupplierReference;
    private String iSubject;
    private String iMessage;
    private String actionResult;
    private static final String email_date_format = "dd MMMM yyyy";

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

            String onlineSupportDefaultEmail = ServletActionContext.getServletContext().getInitParameter("onlineSupportDefaultEmail");
            String smtpHostName = ServletActionContext.getServletContext().getInitParameter("smtpHostName");
            String smtpPort = ServletActionContext.getServletContext().getInitParameter("smtpPort");
            String smtpEmailUser = ServletActionContext.getServletContext().getInitParameter("smtpEmailUser");
            String smtpEmailUserPassword = ServletActionContext.getServletContext().getInitParameter("smtpEmailPassword");

            String[] recipients = {onlineSupportDefaultEmail};

            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailUserPassword);
            String emailMessage = doConstructEmailMessage(iSubject, iSupplierReference, iMessage);
            emailHelper.postMail(iSubject, emailMessage, recipients);
            this.getActionResponse().AssignMessageResult("Your support request has been sent successfully. A member of the CHOX support team will be in touch shortly.");

        } catch (Exception ex) {
            handleException(ex);
            this.getActionResponse().AddError("Please try again.");
        }

        return SUCCESS;
    }

    private String doConstructEmailMessage(String sSubject, String sSupplierReference, String sMessage) {

        StringBuffer emailMsg = new StringBuffer();
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By: " + getAuthenticatedUser().getDisplayName());
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
