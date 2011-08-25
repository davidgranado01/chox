package idas.chox.web.ws;

import com.idaschox.services.chox.Result;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.idaschox.services.chox.Chox;
import com.idaschox.services.chox.SubmissionResult;
import com.idaschox.services.chox.UploadService;
//import javax.xml.bind.JAXBElement;
//import javax.xml.namespace.QName;
import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.feature.Features;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author John
 */
@WebService(serviceName = "UploadService", portName = "UploadServiceSOAP", endpointInterface = "com.idaschox.services.chox.UploadService", targetNamespace = "http://www.idaschox.com/services/CHOX/", wsdlLocation = "WEB-INF/classes/wsdl/chox-webservices.wsdl")
@Features(features = "org.apache.cxf.feature.LoggingFeature")
//@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@SchemaValidation()
public class UploadServiceImpl implements UploadService, ApplicationContextAware {

    static final Logger LOG = LoggerFactory.getLogger(UploadServiceImpl.class);
    private ApplicationContext ctx;

    @Override
    public SubmissionResult uploadBordereau(Chox chox) {


        UploadServiceBean endPointService = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return endPointService.uploadBordereau(chox);

    }

    @Override
    public Result paymentReceived(String supplierReference) {

        UploadServiceBean webServiceEndPointImpl = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return webServiceEndPointImpl.paymentReceived(supplierReference);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ctx = ac;
    }
}
