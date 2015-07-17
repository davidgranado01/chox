package idas.chox.uploadclient;

import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 *
 * @author John
 */
public class Options {

    @Option(name = "-u")
    private String userName;
    @Option(name = "-p")
    private String password;
    @Argument
    private List<String> arguments = new ArrayList<String>();
    @Option(name = "-v", usage = "verbose messages")
    private boolean verbose;
    @Option(name = "-close", usage = "Close claim(s)")
    private boolean close;
    @Option(name = "-reopen", usage = "Re-open claim(s)")
    private boolean reopen;
    @Option(name = "-ecdupdate", usage = "update ECD")
    private boolean updateECD;
    @Option(name = "-paymentreceived", usage = "Payment Received")
    private boolean paymentReceived;
    @Option(name = "-addnote", usage = "Add Note")
    private boolean addNote;
    @Option(name = "-addAttachment", usage = "Add Attachment")
    private boolean addAttachment;
    @Option(name = "-choRef", usage = "CHO Reference for attachment")
    private String choRef;
    @Option(name = "-category", usage = "Attachment Category")
    private String category;
    @Option(name = "-notify", usage = "Attachment Notification")
    private boolean attachmentNotification;
    
    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public boolean isReopen() {
        return reopen;
    }

    public void setReopen(boolean reopen) {
        this.reopen = reopen;
    }

    public boolean isUpdateECD() {
        return updateECD;
    }

    public void setUpdateECD(boolean updateECD) {
        this.updateECD = updateECD;
    }

    public boolean isPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public boolean isAddNote() {
        return addNote;
    }

    public void setAddNote(boolean addNote) {
        this.addNote = addNote;
    }
    
    public boolean isAddAttachment() {
        return addAttachment;
    }

    public void setAddAttachment(boolean addAttachment) {
        this.addAttachment = addAttachment;
    }

    public String getChoRef() {
        return choRef;
    }

    public void setChoRef(String choRef) {
        this.choRef = choRef;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAttachmentNotification() {
        return attachmentNotification;
    }

    public void setAttachmentNotification(boolean attachmentNotification) {
        this.attachmentNotification = attachmentNotification;
    }
}
