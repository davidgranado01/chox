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
        int noAttachments = 0;
        
        // First lets check that we have at least one valid attachment
        if (attachments != null && attachments.size() > 0) {
            noAttachments = attachments.stream().filter((ea) -> (ea.getName().length() > 4)).map((_item) -> 1).reduce(noAttachments, Integer::sum);
        }
        LOG.debug("Processing Total Loss Pack email with {} attachments ({} valid)", attachments.size(), noAttachments);
        
        if (noAttachments==0) {
            LOG.debug("No valid attachments found in email '{}'", subject);
            statusMessages.add("No valid attachments found in email.");
        } else {
            attachments.stream().map((attachment) -> {
                LOG.debug("Processing attachment '{}' of size {}", attachment.getName(), attachment.getSize());
                /* Check is valid referenceNumber provided and claim is in valid status.*/
                Claim claim = null;
                String referenceNumber = "";
                if (attachment.getName().length() < 9) {
                    statusString.append("Ignoring Invalid attachment: ").append(attachment.getName());
                } else {
                    referenceNumber = attachment.getName().substring(4, attachment.getName().length() - 4);
                    claim = validateClaimReferenceNumber(referenceNumber, statusString);
                }
                /* Check Claim Status */
                if (claim != null && (ClaimStatus.CLAIM_CLOSED.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_REJECTION_ACCEPTED.equals(claim.getStatus())
                        || ClaimStatus.INVOICE_REJECTED_ACCEPTED.equals(claim.getStatus())
                        || ClaimStatus.INVOICE_PAYMENT_RECEIVED.equals(claim.getStatus())
                        || ClaimStatus.MANUAL_INVOICE_PAID.equals(claim.getStatus()))) {
                    LOG.debug("Cannot add attachment to claim {} in status '{}'", claim.getChoReference(), claim.getStatus());
                    statusString.append("Claim '").append(claim.getChoReference()).append("' is closed");
                }
                /* Validate attachment file size */
                if (attachment.getSize() > FileHelper.MAX_FILE_SIZE_ALLOW) {
                    LOG.debug("Attachment too big to be added to claim {}: {} > {}", referenceNumber, attachment.getSize(), FileHelper.MAX_FILE_SIZE_ALLOW);
                    statusString.append("Attachment File Size is exceeded ").append(FileHelper.maxFileSize("MB")).append(" MB limit.");
                }
                /* If no errors, upload attachment */
                if (statusString.toString().isEmpty() && claim != null) {
                    claim.setTotalLossChase(true);
                    if (claim.getHireMonitoringDetail() == null) {
                        claim.setHireMonitoringDetail(new HireMonitoringDetail());
                    }

                    if (!claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                        LOG.debug("Marking claim {} as total loss", claim.getChoReference());
                        claimService.setTotalLoss(claim, true);
                        statusString.append("Claim has been marked as a total loss. ");
                    }

                    try {
                        ((SecureDataService) attachmentService).setSecurityInfoProvider(((SecureDataService) attachmentService).getSecurityInfoProvider());
                        String result = attachmentService.addAttachment(claim, attachment.getContent(), attachment.getName(),
                                attachment.getSize(), AttachmentCategory.ATTCAT_TOTALLOSS_PACK, "Total Loss pack has been uploaded for review.", true, false, "CHO");
                        if (result == null) {
                            LOG.debug("Total Loss Pack attachment added to claim {}", claim.getChoReference());
                            statusString.insert(0, attachment.getName() + "\tSuccess: Attachment file has been uploaded on claim '" + claim.getChoReference() +"'. ");
                            // Add event to event log - currently no attachment added event
//                        ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(new AttachmentAddedEvent(claim, "TotalLossPack", ?));
                        } else {
                            LOG.debug("Adding Total Loss Pack attachment to claim {} result: {}", claim.getChoReference(), result);
                            statusString.insert(0, attachment.getName() + "\t" + result);
                        }
                    } catch (Exception ex) {
                        statusString.setLength(0);
                        statusString.append(attachment.getName()).append("\tAn Internal Error Occurred.");
                        LOG.warn("Exception occurred when adding attachment via email scheduler total loss pack job", ex);
                    }
                } else {
                    LOG.debug("Not adding attachment to claim '{}' due to following error: {}", referenceNumber, statusString);
                    statusString.insert(0, attachment.getName() + "\tFailed: ");
                }
                return attachment;
            }).map((_item) -> {
                LOG.debug("Adding message '{}'", statusString);
                statusMessages.add(statusString.toString());
                return _item;
            }).forEachOrdered((_item) -> {
                LOG.debug("Status Message cleared.");
                statusString.setLength(0);
            });
        }
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
