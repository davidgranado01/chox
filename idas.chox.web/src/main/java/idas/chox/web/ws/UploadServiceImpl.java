package idas.chox.web.ws;

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
//import javax.xml.bind.JAXBElement;
//import javax.xml.namespace.QName;
import idas.chox.core.services.UploadClaimXMLService;
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

    UploadClaimXMLService uploadClaimXMLService;

    public void setUploadClaimXMLService(UploadClaimXMLService uploadClaimXMLService) {
        this.uploadClaimXMLService = uploadClaimXMLService;
    }
    
    @Override
    public SubmissionResult uploadBordereau(Chox chox) {
        SubmissionResult result = new SubmissionResult();

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
//            m.marshal(parameters, System.out);
            

            result.setUploadStatus(ClaimUploadStatus.NEW_CLAIM);
            result.setClaimStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            result.setProcessStatus(ClaimProcessStatus.UPLOADED);
            result.setStatus(true);
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
        }


        return result;

    }
}
