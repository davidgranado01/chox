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

    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }
    /*
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    */
    
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
    
    private Attachment getAttachmentFileName(String fileId){
        Attachment att = service.getObject(20);
        return att;
    }
    
    public static void main(String[] args) throws IOException {
        //AttachmentGeneratorAction thisCtrl = new AttachmentGeneratorAction();
        //thisCtrl.doExportFile();
    }
    
    @Override
    public String execute() throws Exception {
        
        
        
        if(this.fileId=="" || this.fileId==null){
            return "error";
        }
        
        Attachment att = getAttachmentFileName(this.fileId);
        if(att==null){
            return "error";
        }
        
        
        
        String strFile = getFileDirectory() + att.getFileName();
        //fileStream = doExportFile(strFile.trim());
        return "error";
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
