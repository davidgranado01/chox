package chox.web.actions;

import chox.model.Attachment;
import chox.services.AttachmentService;
import chox.services.GlobalConfigurationService;
import chox.model.GlobalConfiguration;
import chox.web.viewdata.ActionResponse;
import java.io.File;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class DeleteAttachmentAction extends BaseAction implements SessionAware {
    private String fileId;
    private AttachmentService service;
    private GlobalConfigurationService globalConfigurationService;
    private Map session;
    private String claimid;
   
    public String getClaimid() {
        return claimid;
    }

    public void setClaimid(String claimid) {
        this.claimid = claimid;
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
    
    public GlobalConfigurationService getGlobalConfigurationService() {
        return globalConfigurationService;
    }

    public void setGlobalConfigurationService(GlobalConfigurationService globalConfigurationService) {
        this.globalConfigurationService = globalConfigurationService;
    }
    
    private Attachment getAttachmentFile(int fileId){
        Attachment att = service.getObject(fileId);
        return att;
    }
    
    private String getFileDirectory(){
        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachment_path");
        String attachmentPath = gc.getValue();
        return attachmentPath;
    }
    
    @Override
    public String execute() throws Exception {

        try {
            if (!fileId.equalsIgnoreCase("") && fileId != null) {
                int ifileId = Integer.parseInt(fileId);
                Attachment att = getAttachmentFile(ifileId);

                claimid = String.valueOf(att.getClaim().getId());

                if (!service.deleteAttachment(att)) {
                    return "error";
                } else {
                    String filePath = getFileDirectory() + att.getFileName();
                    File thisFile = new File(filePath);
                    if (thisFile.exists()) {
                        thisFile.delete();
                    }
                }
            }
        } catch (Exception ex) {
             getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    public void setSession(Map arg0) {
        this.session = session;
    }    
}
