package idas.chox.web.ws;

import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.ClaimProcessStatus;
import com.idaschox.services.chox.ClaimStatus;
import com.idaschox.services.chox.ClaimUploadStatus;
import com.idaschox.services.chox.SubmissionResult;
import com.idaschox.services.chox.UploadService;
import com.idaschox.services.chox.SubmissionResult.Messages;
//import javax.xml.bind.JAXBElement;
//import javax.xml.namespace.QName;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.model.WebBordereau;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.WebBordereauService;
import java.io.ByteArrayInputStream;
import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.feature.Features;

/**
 *
 * @author John
 */
@WebService(serviceName = "UploadService", portName = "UploadServiceSOAP", endpointInterface = "com.idaschox.services.chox.UploadService", targetNamespace = "http://www.idaschox.com/services/CHOX/", wsdlLocation = "WEB-INF/classes/wsdl/chox-webservices.wsdl")
@Features(features = "org.apache.cxf.feature.LoggingFeature")
//@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@SchemaValidation()
public class UploadServiceImpl implements UploadService {

    static final Logger LOG = LoggerFactory.getLogger(UploadServiceImpl.class);
    static final String ENCODING = "ISO-8859-1";

    UploadClaimXMLService uploadClaimXMLService;
    WebBordereauService webBordereauService;

    public void setUploadClaimXMLService(UploadClaimXMLService uploadClaimXMLService) {
        this.uploadClaimXMLService = uploadClaimXMLService;
    }

    public void setWebBordereauService(WebBordereauService webBordereauService) {
        this.webBordereauService = webBordereauService;
    }


    @Override
    public SubmissionResult uploadBordereau(Chox chox) {
        SubmissionResult result = new SubmissionResult();
        WebBordereau webBordereau = new WebBordereau();
        
        LOG.info("uploadBordereau called with chox: {}", chox);
        LOG.info("uploadClaimXMLService is : {}", uploadClaimXMLService);
        JAXBContext context = null;
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

            LOG.info("File uploaded status: {}", uploadResult.isValid());
            
            // Convert uploadResult
            if (uploadResult.getRemark().equals("New Claim"))
                result.setUploadStatus(ClaimUploadStatus.NEW_CLAIM);
            else if (uploadResult.getRemark().equals("Hire Monitoring and New Invoice"))
                result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING_AND_NEW_INVOICE);
            else if (uploadResult.getRemark().equals("New Invoice"))
                result.setUploadStatus(ClaimUploadStatus.NEW_INVOICE);
            else if (uploadResult.getRemark().equals("Hire Monitoring"))
                result.setUploadStatus(ClaimUploadStatus.HIRE_MONITORING);
            else if (uploadResult.getRemark().equals("Claim Closed or Pending"))
                result.setUploadStatus(ClaimUploadStatus.CLAIM_CLOSED_OR_PENDING);
            else if (uploadResult.getRemark().equals("Claim Already Exists"))
                result.setUploadStatus(ClaimUploadStatus.CLAIM_ALREADY_EXISTS);
            else if (uploadResult.getRemark().equals("Invoice Already Exists"))
                result.setUploadStatus(ClaimUploadStatus.INVOICE_ALREADY_EXISTS);
            else if (uploadResult.getRemark().equals("Incorrect XML Structure"))
                result.setUploadStatus(ClaimUploadStatus.INCORRECT_XML_STRUCTURE);
            else if (uploadResult.getRemark().equals("Invalid Claim Status"))
                result.setUploadStatus(ClaimUploadStatus.INVALID_CLAIM_STATUS);
            else if (uploadResult.getRemark().equals("Incorrect Value Provided for Hire State"))
                result.setUploadStatus(ClaimUploadStatus.INCORRECT_VALUE_PROVIDED_FOR_HIRE_STATE);
            else if (uploadResult.getRemark().equals("Insurer is not accepting TPI invoice"))
                result.setUploadStatus(ClaimUploadStatus.INSURER_IS_NOT_ACCEPTING_TPI_INVOICE);
            else if (uploadResult.getRemark().equals("New TPI Claim"))
                result.setUploadStatus(ClaimUploadStatus.NEW_TPI_CLAIM);
            else if (uploadResult.getRemark().equals("Supplementary Invoice Already Exists"))
                result.setUploadStatus(ClaimUploadStatus.SUPPLEMENTARY_INVOICE_ALREADY_EXISTS);
            else if (uploadResult.getRemark().equals("New Supplementary Invoice"))
                result.setUploadStatus(ClaimUploadStatus.NEW_SUPPLEMENTARY_INVOICE);
            else if (uploadResult.getRemark().equals("Error"))
                result.setUploadStatus(ClaimUploadStatus.ERROR);
            else {
                LOG.error("Unknown remark found in upload result: {}", uploadResult.getRemark());
                result.setUploadStatus(null);
            }
      
            if (uploadResult.getClaimStatus().equals("N/A"))
                result.setClaimStatus(ClaimStatus.N_A);
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedUnrouted"))
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedRouted"))
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            else if (uploadResult.getClaimStatus().equals("ClaimRejected"))
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTED);
            else if (uploadResult.getClaimStatus().equals("ClaimRejectionAccepted"))
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
            else if (uploadResult.getClaimStatus().equals("ClaimRejectionContested"))
                result.setClaimStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
            else if (uploadResult.getClaimStatus().equals("AwaitingCarHireInfo"))
                result.setClaimStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            else if (uploadResult.getClaimStatus().equals("AwaitingInvoiceData"))
                result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_DATA);
            else if (uploadResult.getClaimStatus().equals("InvoiceDataCalculationIncorrect"))
                result.setClaimStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
            else if (uploadResult.getClaimStatus().equals("InvoiceApprovedByBRE"))
                result.setClaimStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
            else if (uploadResult.getClaimStatus().equals("InvoiceEscalated"))
                result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED);
            else if (uploadResult.getClaimStatus().equals("InvoiceEscalatedToHandler"))
                result.setClaimStatus(ClaimStatus.INVOICE_ESCALATED_TO_HANDLER);
            else if (uploadResult.getClaimStatus().equals("ContestedInvoiceReferredToInsurer"))
                result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_INSURER);
            else if (uploadResult.getClaimStatus().equals("ContestedInvoiceReferredToCHO"))
                result.setClaimStatus(ClaimStatus.CONTESTED_INVOICE_REFERRED_TO_CHO);
            else if (uploadResult.getClaimStatus().equals("InvoiceRejectionAccepted"))
                result.setClaimStatus(ClaimStatus.INVOICE_REJECTION_ACCEPTED);
            else if (uploadResult.getClaimStatus().equals("AwaitingInvoicePayment"))
                result.setClaimStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            else if (uploadResult.getClaimStatus().equals("InvoicePaymentLogged"))
                result.setClaimStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
            else if (uploadResult.getClaimStatus().equals("ClaimReferredToEngineer"))
                result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_ENGINEER);
            else if (uploadResult.getClaimStatus().equals("ClaimReferredToFNOL"))
                result.setClaimStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            else if (uploadResult.getClaimStatus().equals("ClaimClosed"))
                result.setClaimStatus(ClaimStatus.CLAIM_CLOSED);
            else if (uploadResult.getClaimStatus().equals("ClaimPending"))
                result.setClaimStatus(ClaimStatus.CLAIM_PENDING);
            else if (uploadResult.getClaimStatus().equals("InvoiceReferredToClaimsHandler"))
                result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_CLAIMS_HANDLER);
            else if (uploadResult.getClaimStatus().equals("PaymentReceived"))
                result.setClaimStatus(ClaimStatus.PAYMENT_RECEIVED);
            else if (uploadResult.getClaimStatus().equals("ClaimUpdatedByEngineer"))
                result.setClaimStatus(ClaimStatus.CLAIM_UPDATED_BY_ENGINEER);
            else if (uploadResult.getClaimStatus().equals("InvoiceReferredToEngineer"))
                result.setClaimStatus(ClaimStatus.INVOICE_REFERRED_TO_ENGINEER);
            else if (uploadResult.getClaimStatus().equals("ClaimUnacknowledgedUnassigned"))
                result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
            else if (uploadResult.getClaimStatus().equals("AwaitingLiabilityResolution"))
                result.setClaimStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
            else if (uploadResult.getClaimStatus().equals("InvoiceUnassigned"))
                result.setClaimStatus(ClaimStatus.INVOICE_UNASSIGNED);
            else {
                LOG.error("Unknown claim status found in upload result: {}", uploadResult.getClaimStatus());
                result.setClaimStatus(null);
            }
            
            if (uploadResult.getProcessStatus().equals("Failed"))
                result.setProcessStatus(ClaimProcessStatus.FAILED);
            else if (uploadResult.getProcessStatus().equals("Updated"))
                result.setProcessStatus(ClaimProcessStatus.UPDATED);
            else if (uploadResult.getProcessStatus().equals("Uploaded"))
                result.setProcessStatus(ClaimProcessStatus.UPLOADED);
            else {
                LOG.error("Unknown process status found in upload result: {}", uploadResult.getProcessStatus());
                result.setProcessStatus(null);
            }

            result.setStatus(uploadResult.isValid());
            webBordereau.setClaimStatus(uploadResult.getClaimStatus());
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
            messages.getMessages().add("An internal error has occured processing this request: please contact Support");
            result.setMessages(messages);
            
            webBordereau.setClaimStatus("N/A");
            webBordereau.setHireState("unknown"); // uploadResult.getHireState()
            webBordereau.setProcessStatus("Failed");
            webBordereau.setStatus(false);
            webBordereau.setChoReference(null);
            webBordereau.setMessage("An internal error has occured processing this request: " + ex.getMessage());

        }
        catch (UnsupportedEncodingException ex) {
            LOG.error("UnsupportedEncodingException: {}", ex.getMessage());
            result.setUploadStatus(ClaimUploadStatus.ERROR);
            result.setClaimStatus(ClaimStatus.N_A);
            result.setProcessStatus(ClaimProcessStatus.FAILED);
            result.setStatus(false);
            Messages messages = new Messages();
            messages.getMessages().add("An internal error has occured processing this request: please contact Support");
            result.setMessages(messages);

            webBordereau.setClaimStatus("N/A");
            webBordereau.setHireState("unknown"); // uploadResult.getHireState()
            webBordereau.setProcessStatus("Failed");
            webBordereau.setStatus(false);
            webBordereau.setChoReference(null);
            webBordereau.setMessage("An internal error has occured processing this request: " + ex.getMessage());

        }
        finally {
            webBordereauService.saveBordereau(webBordereau);
        }

        return result;

    }
}
