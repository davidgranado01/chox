package idas.chox.web.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;

public class AttachmentGeneratorAction extends BaseAction {
    
    private String fileId;
    private InputStream fileStream;
    private AttachmentService service;
    private AttachmentTypeService attachmentTypeService;
    
    private String contentDisposition;
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }
   
    private Attachment getAttachmentFileName(int fileId){
        Attachment att = service.getAttachment(fileId);
        return att;
    }
    
    @Override
    public String execute() throws Exception {
        
        int selectedFileId;
        
        if(!fileId.equalsIgnoreCase("") && fileId!=null){
            selectedFileId = Integer.parseInt(this.fileId);
        }else{
            return "error";
        }
        
        Attachment att = getAttachmentFileName(selectedFileId);
        
        if(att==null){
            return "error";
        }
        
        fileStream = new ByteArrayInputStream(att.getAttachment().getFileBuffer());
        String strContentDisposition = "filename="+att.getFileName();
        this.setContentDisposition(strContentDisposition);
        AttachmentType attachmentType = attachmentTypeService.getAttachmentType(att.getFileType());
        
        if(attachmentType!=null){
            this.setContentType(attachmentType.getMimeType());
        }else{
            this.setContentType("text/html");            
        }
        
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
