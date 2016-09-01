package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.util.EmailHelper;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

/**
 *
 * @author John
 */
public class ReferFraudCheck extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ReferFraudCheck.class);
    private String smtpHostName;
    private String smtpPort;
    private String smtpEmailUser;
    private String smtpEmailPassword;
    private String keoghsReceiver;
    private String keoghsBccReceiver;

    public void setSmtpHostName(String smtpHostName) {
        this.smtpHostName = smtpHostName;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpEmailUser(String smtpEmailUser) {
        this.smtpEmailUser = smtpEmailUser;
    }

    public void setSmtpEmailPassword(String smtpEmailPassword) {
        this.smtpEmailPassword = smtpEmailPassword;
    }

    public void setKeoghsReceiver(String keoghsReceiver) {
        this.keoghsReceiver = keoghsReceiver;
    }

    public void setKeoghsBccReceiver(String keoghsBccReceiver) {
        this.keoghsBccReceiver = keoghsBccReceiver;
    }

    
    @Override
    @Secured ({"ROLE_INS"})
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Refer claim '{}' to Keoghs...", claim.getChoReference());
        String subject = "CHOX Fraud Referral from " + claim.getInsurer().getName() + " on claim '" + claim.getChoReference() + "'";
        String message = "Client Batch Reference Number: " + (claim.getKeoghsRequest()==null ? "(not available)" : claim.getKeoghsRequest().getClientBatchReference())
                + "\r\nSupplier Reference: " + claim.getChoReference()
                + "\r\nInsurer Name: " + claim.getInsurer().getName()
                + "\r\nInsurer Handler Name: " + (claim.getClaimOwner() == null ? "(not available)" : claim.getClaimOwner().getFullName())
                + "\r\nTelephone number of Handler: " + (claim.getClaimOwner() == null ? "(not available)" : claim.getClaimOwner().getTelephone() == null ? "(not available)" : claim.getClaimOwner().getTelephone())
                + "\r\nInsurer Claim Number: " + claim.getClaimNumber()
                + "\r\nCHO Name: " + claim.getChorganisation().getName()
                + "\r\nCHO Customer Name: " + claim.getCustomer().getTitle() + " " + claim.getCustomer().getFirstName() + " " + claim.getCustomer().getLastName()
                + "\r\nCHO Customer VRN: " + (claim.getCustomer().getVehicleRegistration()==null ? "(not available)" : claim.getCustomer().getVehicleRegistration());

        LOG.info("Sending email to keoghs '{}':\n{}", keoghsReceiver, message);
        sendMail(subject, message);

        claim.setSentToKeoghs(true);
        claim.addComment(Comment.newComment(1, "This claim has been referred to Keoghs."));
    }
    
    private void sendMail(String subject, String emailMessage) {
        String[] receivers = keoghsReceiver != null ? keoghsReceiver.split(",") : null;
        String[] bccReceivers = keoghsBccReceiver != null ? keoghsBccReceiver.split(",") : null;
        LOG.debug("sending mails to receivers {} and bccreceivers {} ", receivers, bccReceivers);
        try {
            EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword);
            if (bccReceivers != null && bccReceivers.length > 0) {
                emailHelper.postMail(subject, emailMessage, receivers, bccReceivers);
            } else {
                emailHelper.postMail(subject, emailMessage, receivers);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding Exception thrown sending email to Keoghs with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        } catch (MessagingException e) {
            LOG.error("Messaging Exception thrown sending email to Keoghs with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailPassword={}: ",
                    new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailPassword, e});
        }
    }
    
}
