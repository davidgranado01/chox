package chox.web.actions;

import chox.model.Attachment;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import java.io.*;
import chox.model.GlobalConfiguration;
import chox.services.GlobalConfigurationService;
import chox.services.AttachmentService;

public class AttachmentGeneratorAction extends BaseAction implements SessionAware {
    
    private String fileId;
    private InputStream fileStream;
    private Map session;
    private AttachmentService service;
    private GlobalConfigurationService globalConfigurationService;
    
    private String contentDisposition;

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }
    
    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }
        
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public GlobalConfigurationService getGlobalConfigurationService() {
        return globalConfigurationService;
    }

    public void setGlobalConfigurationService(GlobalConfigurationService globalConfigurationService) {
        this.globalConfigurationService = globalConfigurationService;
    }

    public void setSession(Map session) {
        this.session = session;
    }
    
    private String getFileDirectory(){
        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachment_path");
        String attachmentPath = gc.getValue();
        return attachmentPath;
    }
    
    private Attachment getAttachmentFileName(int fileId){
        Attachment att = service.getObject(fileId);
        return att;
    }
    
    @Override
    public String execute() throws Exception {
        
        int fileId = 0;
        
        if(!this.fileId.equalsIgnoreCase("") && this.fileId!=null){
            fileId = Integer.parseInt(this.fileId);
        }else{
            return "error";
        }
        
        Attachment att = getAttachmentFileName(fileId);
        
        if(att==null){
            return "error";
        }
        
        fileStream = new ByteArrayInputStream(att.getFileBuffer());

        String strContentDisposition = "filename="+att.getFileName();
        this.setContentDisposition(strContentDisposition);
        
        return SUCCESS;
    }
    
    public FileInputStream doExportFile(String strFile) throws IOException {
        
        FileInputStream fis = null;
        
        try
        {
            File f = new File(strFile);
            fis = new FileInputStream(f);
        }
        catch (IOException e){
            
        }
        return fis;
    }
}
