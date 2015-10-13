package idas.chox.web.ws;

import javax.jws.WebService;

import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.feature.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.idaschox.services.chox.*;

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


        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.uploadBordereau(chox);

    }

    @Override
    public Result paymentReceived(String supplierReference) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.paymentReceived(supplierReference);
    }

    @Override
    public Result closeClaim(String supplierReference) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.closeClaim(supplierReference);
    }

    @Override
    public Result reopenClaim(String supplierReference) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.reopenClaim(supplierReference);
    }
    
    @Override
    public Result updateECD(EcdParam ecdParam) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.updateECD(ecdParam);
    }
    
    
    @Override
    public Result addNote(Note note) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.addNote(note);
    }

    @Override
    public Result addAttachment(Attachment attachment) {

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        return uploadServiceBean.addAttachment(attachment);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ctx = ac;
    }
}
