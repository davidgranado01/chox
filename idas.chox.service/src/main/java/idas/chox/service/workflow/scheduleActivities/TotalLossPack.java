package idas.chox.service.workflow.scheduleActivities;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.EmailAttachment;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.data.services.SecureDataService;

/**
 *
 * @author john
 */
public class TotalLossPack extends BaseScheduleActivity {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossPack.class);
    private AttachmentService attachmentService;
    private AttachmentTypeService attachmentTypeService;
    private final List<String> statusMessages = new ArrayList<>();
    
    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    @Override
    public boolean process(String body, List<EmailAttachment> attachments, String from, String subject) throws Exception {
        StringBuilder statusString = new StringBuilder();

        attachments.stream().map((attachment) -> {
            /* Check is valid referenceNumber provided and claim is in valid status.*/
            String referenceNumber = attachment.getName().substring(4, attachment.getName().length() - 4);
            LOG.debug("Getting claim {} to add attachment of size {}", referenceNumber, attachment.getSize());
            Claim claim = validateClaimReferenceNumber(referenceNumber, statusString);
            /* Check Claim Status */
            if (claim != null && (ClaimStatus.CLAIM_CLOSED.equals(claim.getStatus())
                    || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus())
                    || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                    || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                    || ClaimStatus.MANUAL_INVOICE_PAID.equals(claim.getStatus()))) {
                statusString.append("Claim is closed");
            }
            /* Validate attachment file size */
            if (attachment.getSize() > FileHelper.MAX_FILE_SIZE_ALLOW) {
                statusString.append("Attachment File Size is exceeded ").append(FileHelper.maxFileSize("MB")).append(" MB limit.");
            }
            /* If no errors, upload attachment */
            if (statusString.toString().isEmpty() && claim != null) {
                claim.setTotalLossChase(true);
                if (claim.getHireMonitoringDetail() == null) {
                    claim.setHireMonitoringDetail(new HireMonitoringDetail());
                }

                if (!claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                    claimService.setTotalLoss(claim, true);
                    statusString.append("; Claim has been marked as a total loss");
                }

                try {
                    ((SecureDataService) attachmentService).setSecurityInfoProvider(((SecureDataService) attachmentService).getSecurityInfoProvider());
                    String result = attachmentService.addAttachment(claim, attachment.getContent(), attachment.getName(),
                            attachment.getSize(), AttachmentCategory.ATTCAT_TOTALLOSS_PACK, "Total Loss pack has been uploaded for review.", true, false, "CHO");
                    if (result == null) {
                        statusString.insert(0, claim.getChoReference() + "\tSuccess: Attachment file has been uploaded against claim " + claim.getChoReference());
                    } else {
                        statusString.insert(0, claim.getChoReference() + "\t" + result);
                    }
                } catch (Exception ex) {
                    statusString.insert(0, claim.getChoReference() + "\tAn Internal Error Occurred");
                    LOG.warn("Exception occurred when adding attachment via email scheduler total loss pack job", ex);
                }
            } else {
                statusString.insert(0, referenceNumber + "\tFailed:");
            }
            return attachment;
        }).map((_item) -> {
            statusMessages.add(statusString.toString());
            return _item;
        }).forEachOrdered((_item) -> {
            statusString.setLength(0);
        });
        return true;
    }

    
    @Override
    public String getResponse(String subject, String from) {
        StringBuilder emailMsg = new StringBuilder();
        emailMsg.append("======================================================================\n");
        emailMsg.append("          CHOX Automation response: Total Loss Pack                   \n");
        emailMsg.append("======================================================================\n");
        emailMsg.append("Submitted By Email: ").append(from).append("\n");
        emailMsg.append("Date: ").append(DateHelper.getCurrentDateWithFormat(EMAIL_DATE_FORMAT)).append("\n");
        emailMsg.append("Subject: ").append(subject).append("\n");
        emailMsg.append("======================================================================\n\n");
        statusMessages.forEach((statusMessage) -> {
            emailMsg.append(statusMessage).append("\n");
        });
        LOG.debug("Message to send is: \n*********\n{}\n*********", emailMsg.toString());
        return emailMsg.toString();
    }

}
