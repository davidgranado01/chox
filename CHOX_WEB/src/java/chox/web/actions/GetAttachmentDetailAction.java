package chox.web.actions;

import chox.model.Attachment;
import chox.services.AttachmentService;
import net.sf.json.JSONObject;

public class GetAttachmentDetailAction extends BaseAction{
    
    private int fileId;
    private Attachment attachment;
    private AttachmentService service;

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
    
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
    
    /*
    public void prepare() throws Exception {
        model = service.getObject(fileId);
    } 
    */
    
    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }
        
    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.attachment);
        return jObject.toString();
    }
    
    @Override
    public String execute(){
        attachment = service.getObject(fileId);
        return SUCCESS;
    }
    
    /*
    public Attachment getModel() {
        return model;
    }
    */
}
