package idas.chox.web.scheduler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.FileHelper;


public class TotalLossNotificationSchedulerJob extends PdfEmailSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossNotificationSchedulerJob.class);
    private ClaimService claimService;
    private AttachmentService attachmentService;
    private AttachmentTypeService attachmentTypeService;
    
    private String REG_ALPHANUMERIC = "^([\\d]|[a-z]|[A-Z]).*$";
    public static final String JOB_NAME = "TOTALLOSS_NOTIFICATION";

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Secured({"ROLE_CHO", "ROLE_CHOX_ADMIN"})
    @Override
    protected String doJob(EmailAttachment attachment, String sender) {
                StringBuilder statusString = new StringBuilder();

                /* Check is valid referenceNumber provided and claim is in valid status.*/
                String referenceNumber = attachment.getName().substring(4, attachment.getName().length()-4);
                LOG.info("Getting claim {} to add attachment", referenceNumber);
                Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);
                
                /* Check Claim Status */
                if (claim != null && (ClaimStatus.CLAIM_CLOSED.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus())
                        || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                        || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                        || ClaimStatus.MANUAL_INVOICE_PAID.equals(claim.getStatus()))
                        ) {
                    statusString.append("Claim is closed");
                }

                /* Validate attachment file size */
                if (attachment.getSize() > FileHelper.MAX_FILE_SIZE_ALLOW) {
                    statusString.append("Attachment File Size is exceeded ").append(FileHelper.maxFileSize("MB")).append(" MB limit.");
                }
                
                /* If no errors, upload attachment */
                if (statusString.toString().isEmpty() && claim != null) {
                    if (claim.getHireMonitoringDetail() == null) {
                        claim.setHireMonitoringDetail(new HireMonitoringDetail());
                    }

                    if (claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                        statusString.append("; Claim is already marked as a total loss");
                    } else {
                        claim.getHireMonitoringDetail().setIsTotalLostCheck(true);
                        claim.getHireMonitoringDetail().setIsTotalLostCheckLastModified(new Date());
                        claim.getCustomer().setIsTotalLoss(Boolean.TRUE);
                    }
                    
                    try {
                        boolean result = attachmentService.addAttachment(claim, attachment.getIs(), attachment.getName(),
                                attachment.getSize(), AttachmentCategory.ATTCAT_TOTALLOSS_NOTIFICATION, "Attachment Remark: text to be provided by ER", false, false, "system");
                        if (result) {
                            statusString.insert(0, claim.getChoReference() + "\tSuccess: Attachemnt file has been uploaded against claim "+claim.getChoReference());
                        } else {
                            statusString.insert(0, claim.getChoReference() + "\tFailed: An Internal Error Occurred");
                        }
                    } catch (Exception ex) {
                        statusString.insert(0, claim.getChoReference() + "\tAn Internal Error Occurred");
                        LOG.warn("Exception occurred when adding attachment via email scheduler total loss notification job", ex);
                    }
                } else {
                    statusString.insert(0, referenceNumber + "\tFailed:");
                }

        return statusString.toString();
    }

    @Override
    protected String buildMessage(String email, String subject, String[] statusMessages) {
        
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("          CHOX Automation response: Total Loss Notification           \n");
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(email).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(email_date_format)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        for (String statusMessage : statusMessages) {
            emailMsg.append(statusMessage).append("\n");
        }
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

    private Claim validateClaimReferenceNumber(String referenceNumber, StringBuilder statusString) {

        Claim claim = null;
        if (!regexExpressionChecker(REG_ALPHANUMERIC, referenceNumber)) {
            statusString.append(" No Claim Reference Provided.");
        } else {
            claim = claimService.getClaimByCHOReferenceNumber(referenceNumber);

            if (claim == null) {
                LOG.debug("No Such Claim Reference {}", referenceNumber);
                statusString.append(" No Such Claim Reference.");
            }
        }
        return claim;
    }
    
    private boolean regexExpressionChecker(String regex, String dataValue) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dataValue);

        if (!m.find()) {
            LOG.debug("Invalid data for regex '{}': {}", regex, dataValue);
            return false;
        }
        return true;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}
