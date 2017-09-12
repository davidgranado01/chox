package idas.chox.web.ws;

import javax.jws.WebService;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.cxf.annotations.SchemaValidation;
import org.apache.cxf.feature.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.idaschox.services.chox.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
        String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(supplierReference, Whitelist.basic()));
        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.paymentReceived(clean);
    }

    @Override
    public Result closeClaim(String supplierReference) {
        String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(supplierReference, Whitelist.basic()));

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.closeClaim(clean);
    }

    @Override
    public Result reopenClaim(String supplierReference) {
        String clean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(supplierReference, Whitelist.basic()));

        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.reopenClaim(clean);
    }
    
    @Override
    public Result updateECD(EcdParam ecdParam) {
        ecdParam.setDelayReason(StringEscapeUtils.unescapeHtml4(Jsoup.clean(ecdParam.getDelayReason(), Whitelist.basic())));
        ecdParam.setSupplierReference(StringEscapeUtils.unescapeHtml4(Jsoup.clean(ecdParam.getSupplierReference(), Whitelist.basic())));
        ecdParam.setSupportingNote(StringEscapeUtils.unescapeHtml4(Jsoup.clean(ecdParam.getSupportingNote(), Whitelist.basic())));
        
        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.updateECD(ecdParam);
    }
    
    
    @Override
    public Result addNote(Note note) {
        note.setComment(StringEscapeUtils.unescapeHtml4(Jsoup.clean(note.getComment(), Whitelist.basic())));
        note.setSupplierReference(StringEscapeUtils.unescapeHtml4(Jsoup.clean(note.getSupplierReference(), Whitelist.basic())));
        note.setVisibility(StringEscapeUtils.unescapeHtml4(Jsoup.clean(note.getVisibility(), Whitelist.basic())));
        
        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.addNote(note);
    }

    @Override
    public Result addAttachment(Attachment attachment) {
        attachment.setFileType(StringEscapeUtils.unescapeHtml4(Jsoup.clean(attachment.getFileType(), Whitelist.basic())));
        attachment.setFilename(StringEscapeUtils.unescapeHtml4(Jsoup.clean(attachment.getFilename(), Whitelist.basic())));
        attachment.setRemark(StringEscapeUtils.unescapeHtml4(Jsoup.clean(attachment.getRemark(), Whitelist.basic())));
        attachment.setSupplierReference(StringEscapeUtils.unescapeHtml4(Jsoup.clean(attachment.getSupplierReference(), Whitelist.basic())));
        
        UploadServiceBean uploadServiceBean = (UploadServiceBean) ctx.getBean("uploadServiceBean");
        
        return uploadServiceBean.addAttachment(attachment);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ctx = ac;
    }
}
