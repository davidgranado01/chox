package idas.chox.web.ws;

import com.idaschox.services.chox.*;
import com.idaschox.services.chox.SubmissionResult.Messages;
import idas.chox.core.model.Claim;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.model.WebBordereau;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.WebBordereauService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.AddNote;
import idas.chox.service.workflow.activities.EcdUpdate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;


public class UploadServiceBean {
    
    static final Logger LOG = LoggerFactory.getLogger(UploadServiceBean.class);
    static final String ENCODING = "ISO-8859-1";
    
    
    private UploadClaimXMLService uploadClaimXMLService;
    private ClaimService claimService;
    private WebBordereauService webBordereauService;
    private ActivityFactory activityFactory;
    private SecurityInfoProvider securityInfoProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
    
    public void setUploadClaimXMLService(UploadClaimXMLService uploadClaimXMLService) {
        this.uploadClaimXMLService = uploadClaimXMLService;
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
        
        LOG.info("uploadBordereau called in {} with chox: {}", this, chox);
        LOG.info("uploadClaimXMLService is : {}", uploadClaimXMLService);
        JAXBContext context;
        try {
//            context = JAXBContext.newInstance(GetSubmissionRequest.class);
            context = JAXBContext.newInstance("com.idaschox.services.chox");
            LOG.debug("Context created.");
            Marshaller m = context.createMarshaller();
            LOG.debug("Marshaller created.");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            StringBuilderOutputStream st = new StringBuilderOutputStream();
            
            LOG.debug("Marshalling...");
            m.marshal(chox, st);

            // If no @XmlRootElement is generated in java code, we'll need to wrap in a JAXBElement
//            m.marshal(new JAXBElement<Chox>(new QName("uri","local"), Chox.class, chox), st);
            LOG.info("Received file for upload :\n{}", st.toString());
            
            byte[] byteArray = st.toString().getBytes(ENCODING); // choose a charset
            webBordereau.setFileBuffer(byteArray);
            webBordereau.setFileSize((long)byteArray.length);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);                        
            UploadedXMLClaimsDetail uploadResult = uploadClaimXMLService.processWebServiceClaim(bais);
            try {
                bais.close();
            } catch (IOException ex) {
                LOG.error("Exception thrown closing web-service bordereau input stream: {}", ex.getMessage(), ex);
            }
            try {
                st.close();
            } catch (IOException ex) {
                LOG.error("Exception thrown closing web-service bordereau stringbuilder output stream: {}", ex.getMessage(), ex);
            }
            LOG.info("File uploaded status: {}", uploadResult.isValid());
            
            // Convert uploadResult
            if (uploadResult.getRemark().equals("New Claim")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_CLAIM);
            }
            else if (uploadResult.getRemark().equals("New Claim (Subscriber)")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_SUBSCRIBER_CLAIM);
            }
            else if (uploadResult.getRemark().equals("Claim Already Exists")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS);
            }
            else if (uploadResult.getRemark().equals("Claim Already Exists (Subscriber)")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_SUBSCRIBER);
            }
            else if (uploadResult.getRemark().equals("Claim Closed or Pending")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_CLOSED_OR_PENDING);
            }
            else if (uploadResult.getRemark().equals("New Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_INVOICE);
            }
            else if (uploadResult.getRemark().equals("Invoice Already Exists")) {
                result.setUploadStatus(ClaimUploadStatus.INVOICE_ALREADY_EXISTS);
            }
            else if (uploadResult.getRemark().equals("Incorrect XML Structure")) {
                result.setUploadStatus(ClaimUploadStatus.INCORRECT_XML_STRUCTURE);
            }
            else if (uploadResult.getRemark().equals("Incorrect Hire State")) {
                result.setUploadStatus(ClaimUploadStatus.INCORRECT_HIRE_STATE);
            }
            else if (uploadResult.getRemark().equals("Incorrect Value Provided for Hire State")) {
                result.setUploadStatus(ClaimUploadStatus.INCORRECT_VALUE_PROVIDED_FOR_HIRE_STATE);
            }
            else if (uploadResult.getRemark().equals("New TPI Claim")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_TPI_CLAIM);
            }
            else if (uploadResult.getRemark().equals("Insurer is not accepting TPI invoices")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_TPI_INVOICES);
            }
            else if (uploadResult.getRemark().equals("Insurer is not accepting Subscriber Claims")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_SUBSCRIBER_CLAIMS);
            }
            else if (uploadResult.getRemark().equals("Hire Monitoring and New Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING_AND_NEW_INVOICE);
            }
            else if (uploadResult.getRemark().equals("Supplementary Invoice Already Exists")) {
                result.setUploadStatus(ClaimUploadStatus.SUPPLEMENTARY_INVOICE_ALREADY_EXISTS);
            }
            else if (uploadResult.getRemark().equals("New Supplementary Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_SUPPLEMENTARY_INVOICE);
            }
            else if (uploadResult.getRemark().equals("Hire Monitoring")) {
                result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING);
            }
            else if (uploadResult.getRemark().equals("Invalid Claim Status")) {
                result.setUploadStatus(ClaimUploadStatus.INVALID_CLAIM_STATUS);
            }
            else if (uploadResult.getRemark().equals("New Invoice (Insurer vs Insurer)")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_INVOICE_INSURER_VS_INSURER);
            }
            else if (uploadResult.getRemark().equals("New Insurer Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_INSURER_INVOICE);
            }
            else if (uploadResult.getRemark().equals("New Insurer Claim")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_INSURER_CLAIM);
            }
            else if (uploadResult.getRemark().equals("Insurer Claim Already Exists")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_CLAIM_ALREADY_EXISTS);
            }
            else if (uploadResult.getRemark().equals("Insurer Hire Monitoring and New Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_HIRE_MONITORING_AND_NEW_INVOICE);
            }
            else if (uploadResult.getRemark().equals("Insurer Hire Monitoring")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_HIRE_MONITORING);
            }
            else if (uploadResult.getRemark().equals("Insurer Invoice Already Exists")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_INVOICE_ALREADY_EXISTS);
            }
            else if (uploadResult.getRemark().equals("New Insurer Supplementary Invoice")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_SUPPLEMENTARY_INVOICE);
            }
            else if (uploadResult.getRemark().equals("New Claim (Fixed Fee)")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_FIXED_FEE_CLAIM);
            }
            else if (uploadResult.getRemark().equals("Insurer is not accepting Fixed Fee Claims")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_FIXED_FEE_CLAIMS);
            }
            else if (uploadResult.getRemark().equals("Claim Already Exists (Fixed Fee)")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_FIXED_FEE);
            }
            else if (uploadResult.getRemark().equals("Claim Already Exists But As A Different Claim Type")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS); // To Be Updated
            }
            else if (uploadResult.getRemark().equals("Error")) {
                result.setUploadStatus(ClaimUploadStatus.ERROR);
            }
            else if (uploadResult.getRemark().equals("New Claim (Collaboration Protocol)")) {
                result.setUploadStatus(ClaimUploadStatus.NEW_COLLABORATION_PROTOCOL_CLAIM);
            }
            else if (uploadResult.getRemark().equals("Claim Already Exists (Collaboration Protocol)")) {
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS_COLLABORATION_PROTOCOL);
            }
            else if (uploadResult.getRemark().equals("Insurer is not accepting Collaboration Protocol Claims")) {
                result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_COLLABORATION_PROTOCOL_CLAIMS);
            }
            else {
                LOG.error("Unknown remark found in upload result: '{}'", uploadResult.getRemark());
                result.setUploadStatus(ClaimUploadStatus.ERROR);
            }
      
            if (uploadResult.getClaimStatus().equals("N/A")) {
                result.setClaimStatus(ClaimStatus.N_A);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedUnrouted")) {
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedRouted")) {
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimRejected")) {
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTED);
            }
            else if (uploadResult.getClaimStatus().equals("SubscriberClaimRejected")) {
                result.setClaimStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimRejectionAccepted")) {
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimRejectionContested")) {
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            }
            else if (uploadResult.getClaimStatus().equals("AwaitingCarHireInfo")) {
                result.setClaimStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            }
            else if (uploadResult.getClaimStatus().equals("AwaitingInvoiceData")) {
                result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_DATA);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceDataCalculationIncorrect")) {
                result.setClaimStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceApprovedByBRE")) {
                result.setClaimStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceEscalated")) {
                result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceEscalatedToHandler")) {
                result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED_TO_HANDLER);
            }
            else if (uploadResult.getClaimStatus().equals("ContestedInvoiceReferredToInsurer")) {
                result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_INSURER);
            }
            else if (uploadResult.getClaimStatus().equals("ContestedInvoiceReferredToCHO")) {
                result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_CHO);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceRejectionAccepted")) {
                result.setClaimStatus(ClaimStatus.INVOICE_REJECTION_ACCEPTED);
            }
            else if (uploadResult.getClaimStatus().equals("AwaitingInvoicePayment")) {
                result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }
            else if (uploadResult.getClaimStatus().equals("InvoicePaymentLogged")) {
                result.setClaimStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimReferredToEngineer")) {
                result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_ENGINEER);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimReferredToFNOL")) {
                result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimClosed")) {
                result.setClaimStatus(ClaimStatus.CLAIM_CLOSED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimPending")) {
                result.setClaimStatus(ClaimStatus.CLAIM_PENDING);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceReferredToClaimsHandler")) {
                result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_CLAIMS_HANDLER);
            }
            else if (uploadResult.getClaimStatus().equals("PaymentReceived")) {
                result.setClaimStatus(ClaimStatus.PAYMENT_RECEIVED);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimUpdatedByEngineer")) {
                result.setClaimStatus(ClaimStatus.CLAIM_UPDATED_BY_ENGINEER);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceReferredToEngineer")) {
                result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_ENGINEER);
            }
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedUnassigned")) {
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
            }
            else if (uploadResult.getClaimStatus().equals("AwaitingLiabilityResolution")) {
                result.setClaimStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
            }
            else if (uploadResult.getClaimStatus().equals("ManualInvoiceBREApproved")) {
                result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_BRE_APPROVED);
            }
            else if (uploadResult.getClaimStatus().equals("ManualInvoiceBRERejected")) {
                result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_BRE_REJECTED);
            }
            else if (uploadResult.getClaimStatus().equals("ManualInvoiceContested")) {
                result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
            }
            else if (uploadResult.getClaimStatus().equals("ManualInvoicePaid")) {
                result.setClaimStatus(ClaimStatus.MANUAL_INVOICE_PAID);
            }
            else if (uploadResult.getClaimStatus().equals("InvoiceUnassigned")) {
                result.setClaimStatus(ClaimStatus.INVOICE_UNASSIGNED);
            }
            else if (uploadResult.getClaimStatus().equals("AwaitingLitigationOutcome")) {
                result.setClaimStatus(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
            }
            else {
                LOG.error("Unknown claim status found in upload result: {}", uploadResult.getClaimStatus());
                result.setClaimStatus(null);
            }
            
            if (uploadResult.getProcessStatus().equals("Failed")) {
                result.setProcessStatus(ClaimProcessStatus.FAILED);
            }
            else if (uploadResult.getProcessStatus().equals("Updated")) {
                result.setProcessStatus(ClaimProcessStatus.UPDATED);
            }
            else if (uploadResult.getProcessStatus().equals("Uploaded")) {
                result.setProcessStatus(ClaimProcessStatus.UPLOADED);
            }
            else {
                LOG.error("Unknown process status found in upload result: {}", uploadResult.getProcessStatus());
                result.setProcessStatus(null);
            }

            result.setStatus(uploadResult.isValid());
            Messages messages = new Messages();
            messages.getMessages().add(uploadResult.getMessage());
            result.setMessages(messages);
            
            webBordereau.setClaimStatus(uploadResult.getClaimStatus());
            webBordereau.setUploadStatus(result.getUploadStatus().toString());
            webBordereau.setHireState("unknown"); // uploadResult.getHireState()
            webBordereau.setProcessStatus(uploadResult.getProcessStatus());
            webBordereau.setStatus(uploadResult.isValid());
            webBordereau.setChoReference(uploadResult.getChoReference());
            webBordereau.setMessage(uploadResult.getMessage());
        }
        catch (JAXBException ex) {
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

        }
        catch (UnsupportedEncodingException ex) {
            LOG.error("UnsupportedEncodingException: {}", ex.getMessage());
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

        }
        finally {
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
            }
            else {
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
            } 
            else {
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to close. Current status is: " + claim.getStatus());
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage("Access Denied processing request: " +ex.getMessage());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage("Error processing request: " +ex.getMessage());
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
            } 
            else {
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage(new StringBuilder().append("Claim is not in correct status to reopen. Current status is: ").append(claim.getStatus()).toString());
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
                ((EcdUpdate)activity).setEcdDate(ecdDate);
                ((EcdUpdate)activity).setReason(delayReason);
                ((EcdUpdate)activity).setSupportingNote(supportingNote);
                ((EcdUpdate)activity).setUpdateInsurer(true);
                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to reopen. Current status is: " + claim.getStatus());
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

                activity.process(claim);
                result.setStatus(true);
            }
        } catch (InvalidClaimStatusException ex) {
            result.setStatus(false);
            result.setErrorMessage("Claim is not in correct status to close. Current status is: " + claim.getStatus());
        } catch (AccessDeniedException ex) {
            result.setStatus(false);
            result.setErrorMessage("Access Denied processing request: " +ex.getMessage());
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage("Error processing request: " +ex.getMessage());
        }

        return result;
    }
}
