package idas.chox.core.model;

import java.io.Serializable;

public class GmailSchedulerJob extends Entity implements Serializable {

    private String jobName;
    private String loginUserName;
    private String loginPassword;
    private String emailSubject;
    private String bccReceivers;
    private String privilegedUsers;
    private String errorMessageReceivers;
    private String processedLabel;
    private boolean active;
    private boolean replyToSender;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isReplyToSender() {
        return replyToSender;
    }

    public void setReplyToSender(boolean replyToSender) {
        this.replyToSender = replyToSender;
    }

    public String getPrivilegedUsers() {
        return privilegedUsers;
    }

    public void setPrivilegedUsers(String privilegedUsers) {
        this.privilegedUsers = privilegedUsers;
    }

    public String getBccReceivers() {
        return bccReceivers;
    }

    public void setBccReceivers(String bccReceivers) {
        this.bccReceivers = bccReceivers;
    }

    public String getErrorMessageReceivers() {
        return errorMessageReceivers;
    }

    public void setErrorMessageReceivers(String errorMessageReceivers) {
        this.errorMessageReceivers = errorMessageReceivers;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getProcessedLabel() {
        return processedLabel;
    }

    public void setProcessedLabel(String processedLabel) {
        this.processedLabel = processedLabel;
    }
}
