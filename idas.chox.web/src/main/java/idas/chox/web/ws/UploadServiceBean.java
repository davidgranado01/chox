package idas.chox.web.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import com.idaschox.services.chox.*;
import com.idaschox.services.chox.SubmissionResult.BREMessages;
import com.idaschox.services.chox.SubmissionResult.Messages;

import idas.chox.core.model.Claim;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.model.WebBordereau;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.WebBordereauService;
import idas.chox.core.util.FileHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.AddNote;
import idas.chox.service.workflow.activities.EcdUpdate;

public class UploadServiceBean {

    static final Logger LOG = LoggerFactory.getLogger(UploadServiceBean.class);
    static final String ENCODING = "ISO-8859-1";

    private UploadClaimXMLService uploadClaimXMLService;
    private AttachmentTypeService attachmentTypeService;
    private AttachmentService attachmentService;
    private ClaimService claimService;
    private BreBandService breBandService;
    private WebBordereauService webBordereauService;
    private ActivityFactory activityFactory;
    private SecurityInfoProvider securityInfoProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    public void setUploadClaimXMLService(UploadClaimXMLService uploadClaimXMLService) {
        this.uploadClaimXMLService = uploadClaimXMLService;
    }

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setWebBordereauService(WebBordereauService webBordereauService) {
        this.webBordereauService = webBordereauService;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public SubmissionResult uploadBordereau(Chox chox) {
        SubmissionResult result = new SubmissionResult();
        WebBordereau webBordereau = new WebBordereau();

        JAXBContext context;
        try {
//            context = JAXBContext.newInstance(GetSubmissionRequest.class);
            context = JAXBContext.newInstance("com.idaschox.services.chox");
            LOG.debug("Context created.");
            Marshaller m = context.createMarshaller();
            LOG.debug("Marshaller created.");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            UploadedXMLClaimsDetail uploadResult;
            try (StringBuilderOutputStream st = new StringBuilderOutputStream()) {

                LOG.debug("Marshalling...");
                m.marshal(chox, st);

                // If no @XmlRootElement is generated in java code, we'll need to wrap in a JAXBElement
//            m.marshal(new JAXBElement<Chox>(new QName("uri","local"), Chox.class, chox), st);
                LOG.info("Received file for upload :\n{}", st.toString());

                byte[] byteArray = st.toString().getBytes(ENCODING); // choose a charset
                webBordereau.setFileBuffer(byteArray);
                webBordereau.setFileSize((long) byteArray.length);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray)) {
                    uploadResult = uploadClaimXMLService.processWebServiceClaim(bais);
                }
                LOG.info("File uploaded status: {}", uploadResult.isValid());
            } catch (IOException ex) {
                LOG.error("Exception thrown closing web-service bordereau stringbuilder output stream: {}", ex.getMessage(), ex);
                result.setUploadStatus(ClaimUploadStatus.ERROR);
                result.setClaimStatus(ClaimStatus.N_A);
                result.setProcessStatus(ClaimProcessStatus.FAILED);
                result.setStatus(false);
                Messages messages = new Messages();
                messages.getMessages().add("An internal error has occurred processing this request: please contact Support");
                result.setMessages(messages);
                return result;
            }

            // Convert uploadResult
            switch (uploadResult.getRemark()) {
                case "New Claim":
                    result.setUploadStatus(ClaimUploadStatus.NEW_CLAIM);
                    break;
                case "New Claim (Subscriber)":
                    result.setUploadStatus(ClaimUploadStatus.NEW_SUBSCRIBER_CLAIM);
                    break;
                case "Claim Already Exists":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS);
                    break;
                case "Claim Already Exists (Subscriber)":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_SUBSCRIBER);
                    break;
                case "Claim Closed or Pending":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_CLOSED_OR_PENDING);
                    break;
                case "New Invoice":
                    result.setUploadStatus(ClaimUploadStatus.NEW_INVOICE);
                    break;
                case "Invoice Already Exists":
                    result.setUploadStatus(ClaimUploadStatus.INVOICE_ALREADY_EXISTS);
                    break;
                case "Incorrect XML Structure":
                    result.setUploadStatus(ClaimUploadStatus.INCORRECT_XML_STRUCTURE);
                    break;
                case "Incorrect Hire State":
                    result.setUploadStatus(ClaimUploadStatus.INCORRECT_HIRE_STATE);
                    break;
                case "Incorrect Value Provided for Hire State":
                    result.setUploadStatus(ClaimUploadStatus.INCORRECT_VALUE_PROVIDED_FOR_HIRE_STATE);
                    break;
                case "New TPI Claim":
                    result.setUploadStatus(ClaimUploadStatus.NEW_TPI_CLAIM);
                    break;
                case "Insurer is not accepting TPI invoices":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_TPI_INVOICES);
                    break;
                case "Insurer is not accepting Subscriber Claims":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_SUBSCRIBER_CLAIMS);
                    break;
                case "Hire Monitoring and New Invoice":
                    result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING_AND_NEW_INVOICE);
                    break;
                case "Supplementary Invoice Already Exists":
                    result.setUploadStatus(ClaimUploadStatus.SUPPLEMENTARY_INVOICE_ALREADY_EXISTS);
                    break;
                case "New Supplementary Invoice":
                    result.setUploadStatus(ClaimUploadStatus.NEW_SUPPLEMENTARY_INVOICE);
                    break;
                case "Hire Monitoring":
                    result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING);
                    break;
                case "Invalid Claim Status":
                    result.setUploadStatus(ClaimUploadStatus.INVALID_CLAIM_STATUS);
                    break;
                case "New Invoice (Insurer vs Insurer)":
                    result.setUploadStatus(ClaimUploadStatus.NEW_INVOICE_INSURER_VS_INSURER);
                    break;
                case "New Insurer Invoice":
                    result.setUploadStatus(ClaimUploadStatus.NEW_INSURER_INVOICE);
                    break;
                case "New Insurer Claim":
                    result.setUploadStatus(ClaimUploadStatus.NEW_INSURER_CLAIM);
                    break;
                case "Insurer Claim Already Exists":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_CLAIM_ALREADY_EXISTS);
                    break;
                case "Insurer Hire Monitoring and New Invoice":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE);
                    break;
                case "Insurer Hire Monitoring":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_HIRE_MONITORING);
                    break;
                case "Insurer Invoice Already Exists":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_INVOICE_ALREADY_EXISTS);
                    break;
                case "New Insurer Supplementary Invoice":
                    result.setUploadStatus(ClaimUploadStatus.NEW_SUPPLEMENTARY_INVOICE);
                    break;
                case "New Claim (Fixed Fee)":
                    result.setUploadStatus(ClaimUploadStatus.NEW_FIXED_FEE_CLAIM);
                    break;
                case "Insurer is not accepting Fixed Fee Claims":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_FIXED_FEE_CLAIMS);
                    break;
                case "Claim Already Exists (Fixed Fee)":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_FIXED_FEE);
                    break;
                case "Claim Already Exists But As A Different Claim Type":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS); // To Be Updated
                    break;
                case "Error":
                    result.setUploadStatus(ClaimUploadStatus.ERROR);
                    break;
                case "New Claim (Collaboration Protocol)":
                    result.setUploadStatus(ClaimUploadStatus.NEW_COLLABORATION_PROTOCOL_CLAIM);
                    break;
                case "Claim Already Exists (Collaboration Protocol)":
                    result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_COLLABORATION_PROTOCOL);
                    break;
                case "Insurer is not accepting Collaboration Protocol Claims":
                    result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_COLLABORATION_PROTOCOL_CLAIMS);
                    break;
                default:
                    LOG.error("Unknown remark found in upload result: '{}'", uploadResult.getRemark());
                    result.setUploadStatus(ClaimUploadStatus.ERROR);
                    break;
            }

            switch (uploadResult.getClaimStatus()) {
                case "N/A":
                    result.setClaimStatus(ClaimStatus.N_A);
                    break;
                case "ClaimUnacknowledgedUnrouted":
                    result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
                    break;
                case "ClaimUnacknowledgedRouted":
                    result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                    break;
                case "ClaimRejected":
                    result.setClaimStatus(ClaimStatus.CLAIM_REJECTED);
                    break;
                case "SubscriberClaimRejected":
                    result.setClaimStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
                    break;
                case "ClaimRejectionAccepted":
                    result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
                    break;
                case "ClaimRejectionContested":
                    result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
                    break;
                case "AwaitingCarHireInfo":
                    result.setClaimStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
                    break;
                case "AwaitingInvoiceData":
                    result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_DATA);
                    break;
                case "InvoiceDataCalculationIncorrect":
                    result.setClaimStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
                    break;
                case "InvoiceApprovedByBRE":
                    result.setClaimStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
                    break;
                case "InvoiceEscalated":
                    result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED);
                    break;
                case "InvoiceEscalatedToHandler":
                    result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED_TO_HANDLER);
                    break;
                case "ContestedInvoiceReferredToInsurer":
                    result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_INSURER);
                    break;
                case "ContestedInvoiceReferredToCHO":
                    result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_CHO);
                    break;
                case "InvoiceRejectionAccepted":
                    result.setClaimStatus(ClaimStatus.INVOICE_REJECTION_ACCEPTED);
                    break;
                case "AwaitingInvoicePayment":
                    result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                    break;
                case "InvoicePaymentLogged":
                    result.setClaimStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
                    break;
                case "ClaimReferredToEngineer":
                    result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_ENGINEER);
                    break;
                case "ClaimReferredToFNOL":
                    result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
                    break;
                case "ClaimClosed":
                    result.setClaimStatus(ClaimStatus.CLAIM_CLOSED);
                    break;
                case "ClaimPending":
                    result.setClaimStatus(ClaimStatus.CLAIM_PENDING);
                    break;
                case "InvoiceReferredToClaimsHandler":
                    result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_CLAIMS_HANDLER);
                    break;
                case "PaymentReceived":
                    result.setClaimStatus(ClaimStatus.PAYMENT_RECEIVED);
                    break;
                case "ClaimUpdatedByEngineer":
                    result.setClaimStatus(ClaimStatus.CLAIM_UPDATED_BY_ENGINEER);
                    break;
                case "InvoiceReferredToEngineer":
                    result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_ENGINEER);
                    break;
                case "ClaimUnacknowledgedUnassigned":
                    result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
                    break;
                case "AwaitingLiabilityResolution":
                    result.setClaimStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
                    break;
                case "ManualInvoiceBREApproved":
                    result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_BRE_APPROVED);
                    break;
                case "ManualInvoiceBRERejected":
                    result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_BRE_REJECTED);
                    break;
                case "ManualInvoiceContested":
                    result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
                    break;
                case "ManualInvoicePaid":
                    result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_PAID);
                    break;
                case "InvoiceUnassigned":
                    result.setClaimStatus(ClaimStatus.INVOICE_UNASSIGNED);
                    break;
                case "AwaitingLitigationOutcome":
                    result.setClaimStatus(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
                    break;
                default:
                    LOG.error("Unknown claim status found in upload result: {}", uploadResult.getClaimStatus());
                    result.setClaimStatus(null);
                    break;
            }

            switch (uploadResult.getProcessStatus()) {
                case "Failed":
                    result.setProcessStatus(ClaimProcessStatus.FAILED);
                    break;
                case "Updated":
                    result.setProcessStatus(ClaimProcessStatus.UPDATED);
                    break;
                case "Uploaded":
                    result.setProcessStatus(ClaimProcessStatus.UPLOADED);
                    break;
                default:
                    LOG.error("Unknown process status found in upload result: {}", uploadResult.getProcessStatus());
                    result.setProcessStatus(null);
                    break;
            }

            result.setStatus(uploadResult.isValid());
            Messages messages = new Messages();
            messages.getMessages().add(uploadResult.getMessage());
            result.setMessages(messages);

            BREMessages breMessages = new BREMessages();
            String breFailureMessages = uploadResult.getBreFailureMessages();
            if (breFailureMessages != null && !breFailureMessages.isEmpty()) {
                breMessages.getMessages().add(breFailureMessages);
                result.setBREMessages(breMessages);
            }
            webBordereau.setClaimStatus(uploadResult.getClaimStatus());
            webBordereau.setUploadStatus(result.getUploadStatus().toString());
            webBordereau.setHireState("unknown"); // uploadResult.getHireState()
            webBordereau.setProcessStatus(uploadResult.getProcessStatus());
            webBordereau.setStatus(uploadResult.isValid());
            webBordereau.setChoReference(uploadResult.getChoReference());
            webBordereau.setMessage(uploadResult.getMessage());
        } catch (JAXBException ex) {
            LOG.error("Error creating JAXB context: {}", ex.getMessage());
            if (ex.getLinkedException() != null) {
                LOG.error("Linked exception: {}", ex.getLinkedException().getMessage());
            }
            result.setUploadStatus(ClaimUploadStatus.ERROR);
            result.setClaimStatus(ClaimStatus.N_A);
            result.setProcessStatus(ClaimProcessStatus.FAILED);
            result.setStatus(false);
            Messages messages = new Messages();
            messages.getMessages().add("An internal error has occurred processing this request: please contact Support");
            result.setMessages(messages);

            webBordereau.setClaimStatus("N/A");
            webBordereau.setUploadStatus(result.getUploadStatus().toString());
            webBordereau.setHireState("unknown"); // uploadResult.getHireState()
            webBordereau.setProcessStatus("Failed");
            webBordereau.setStatus(false);
            webBordereau.setChoReference(null);
            webBordereau.setMessage("An internal error has occurred processing this request: " + ex.getMessage());

        } finally {
            webBordereauService.saveBordereau(webBordereau);
        }

        return result;

    }

    public Result paymentReceived(String supplierReference) {
        Result result = new Result();

        Activity activity = activityFactory.getActivity("invoicePaymentReceived");

        // Get the claim
        try {
            Claim claim = claimService.getClaimByCHOReferenceNumber(supplierReference);
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage("Claim with supplier reference number '" + supplierReference + "' does not exist.");
            } else if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED.value())) {
                result.setStatus(false);
                result.setErrorMessage("Claim is not in correct status to move into 'Payment Received' (should be '"
                        + ClaimStatus.INVOICE_PAYMENT_LOGGED.value() + "' but is '" + claim.getStatus() + "')");
            } else {
                claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
        }
        return result;
    }

    public Result closeClaim(String supplierReference) {
        Result result = new Result();

        Activity activity = activityFactory.getActivity("closeClaim");
        Claim claim = null;

        // Get the claim
        try {
            claim = claimService.getClaimByCHOReferenceNumber(supplierReference);
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage("Claim with supplier reference number '" + supplierReference + "' does not exist.");
            } else {
                claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to close. Current status is: " + (claim == null ? "null" : claim.getStatus()));
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage("Access Denied processing request: " + ex.getMessage());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage("Error processing request: " + ex.getMessage());
        }

        return result;
    }

    public Result reopenClaim(String supplierReference) {
        Result result = new Result();

        Activity activity = activityFactory.getActivity("reopenClaim");
        Claim claim = null;

        // Get the claim
        try {
            claim = claimService.getClaimByCHOReferenceNumber(supplierReference);
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage(new StringBuilder().append("Claim with supplier reference number '").append(supplierReference).append("' does not exist.").toString());
            } else {
                claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Claim is not in correct status to reopen. Current status is: ").append(claim == null ? "null" : claim.getStatus()).toString());
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Access Denied processing request: ").append(ex.getMessage()).toString());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Error processing request: ").append(ex.getMessage()).toString());
        }

        return result;
    }

    public Result updateECD(EcdParam ecdParam) {

        Activity activity = activityFactory.getActivity("ecdUpdate");

        Result result = new Result();
        Claim claim = null;
        String supplierReference = ecdParam.getSupplierReference();
        Date ecdDate = ecdParam.getEcdDate().toGregorianCalendar().getTime();
        String delayReason = ecdParam.getDelayReason();
        String supportingNote = ecdParam.getSupportingNote();

        try {
            claim = claimService.getClaimByCHOReferenceNumber(supplierReference);
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage("Claim with supplier reference number '" + supplierReference + "' does not exist.");
            } else {
                LOG.debug("ecd date {} ecd reason {} ecd supportnote {}", new Object[]{ecdDate.toString(), delayReason, supportingNote});
                ((EcdUpdate) activity).setEcdDate(ecdDate);
                ((EcdUpdate) activity).setReason(delayReason);
                ((EcdUpdate) activity).setSupportingNote(supportingNote);
                ((EcdUpdate) activity).setUpdateInsurer(true);
                claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to reopen. Current status is: " + (claim == null ? "null" : claim.getStatus()));
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Access Denied processing request: ").append(ex.getMessage()).toString());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Error processing request: ").append(ex.getMessage()).toString());
        }

        return result;
    }

    public Result addNote(Note note) {
        Result result = new Result();

        Activity activity = activityFactory.getActivity("addNote");
        Claim claim = null;

        // Get the claim
        try {
            claim = claimService.getClaimByCHOReferenceNumber(note.getSupplierReference());
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage("Claim with supplier reference number '" + note.getSupplierReference() + "' does not exist.");
            } else {
                int visibilityType = 0;
                if (note.getVisibility() != null && !note.getVisibility().equalsIgnoreCase("private")
                        && !note.getVisibility().equalsIgnoreCase("public") && !note.getVisibility().isEmpty()) {
                    throw new Exception("Only 'public'/'private'  Are Allowed For "
                            + "Visibility Type. Empty strings are also allowed "
                            + "and will be interpreted as 'public'.");
                }

                if (securityInfoProvider.getIsINS()) {
                    if (note.getVisibility() != null && note.getVisibility().equalsIgnoreCase("private")) {
                        visibilityType = 1;
                    }
                } else if (securityInfoProvider.getIsCHO()) {
                    if (note.getVisibility() != null && note.getVisibility().equalsIgnoreCase("private")) {
                        visibilityType = 2;
                    }
                } else {
                    throw new Exception("Only users from Insurer/CHO organisation can add note.");
                }

                ((AddNote) activity).setComment(note.getComment());
                ((AddNote) activity).setVisibilityType(visibilityType);

                claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to close. Current status is: " + (claim == null ? "null" : claim.getStatus()));
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage("Access Denied processing request: " + ex.getMessage());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage("Error processing request: " + ex.getMessage());
        }

        return result;
    }

    public Result addAttachment(com.idaschox.services.chox.Attachment attachment) {
        Result result = new Result();

        try {
            // Get the claim
            Claim claim = claimService.getClaimByCHOReferenceNumber(attachment.getSupplierReference());
            if (claim == null) {
                result.setStatus(false);
                result.setErrorMessage("Claim with supplier reference number '" + attachment.getSupplierReference() + "' does not exist.");
                return result;
            }
            byte[] b;
            LOG.debug("Attachment name is {} with category '{}'", attachment.getFilename(), attachment.getCategory());

            DataHandler handler = attachment.getAttachment();
            try {
                InputStream is = handler.getInputStream();
                b = readFully(is);
            } catch (IOException e) {
                LOG.error("Exception thrown converting stream to byte array: {}", e.getMessage(), e);
                throw e;
            }
            LOG.debug("Attachment read - size={}", b.length);
            // Check size and extension
            String fileName = attachment.getFilename() + "." + attachment.getFileType();
            List<String> attTypes = attachmentTypeService.getAttachmentTypeCode();
            if (!FileHelper.isFileTypeAllow(fileName, attTypes)) {
                throw new Exception("Invalid File type");
            }
            LOG.debug("Filetype is ok: {}", fileName);
            if (b.length > FileHelper.MAX_FILE_SIZE_ALLOW) {
                throw new Exception("File Size is exceeded " + FileHelper.maxFileSize("MB") + " MB limit.");
            }
            LOG.debug("Length is Ok: {}", b.length);
            String whoCreated = "Insurer";
            if (securityInfoProvider.getIsCHO()) {
                whoCreated = "CHO";
            }
            // Add attachment
            LOG.debug("Adding attachment....");
            String status = attachmentService.addAttachment(claim, b, fileName, b.length,
                    attachment.getCategory().value(), attachment.getRemark(), attachment.isNotify(),
                    securityInfoProvider.getIsINS(), whoCreated);
            if (status != null) {
                LOG.error("Error adding attachment received through web-service for claim '{}': {}", claim.getChoReference(), result);
                throw new Exception("Unknown Error occurred, please try again.");
            } else {
                result.setStatus(true);
            }
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage("Access Denied processing request: " + ex.getMessage());
            LOG.error("Access Denied processing request: {}", ex.getMessage());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage("Error processing request: " + ex.getMessage());
            LOG.error("Error processing request: {}", ex.getMessage());
        }
        return result;
    }

    private static byte[] readFully(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        byte[] result;
        int bytesRead;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            result = output.toByteArray();
        }
        return result;
    }
}
