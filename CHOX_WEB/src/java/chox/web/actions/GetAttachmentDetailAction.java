package chox.web.actions;

import chox.model.Attachment;
import chox.services.AttachmentService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

public class GetAttachmentDetailAction extends BaseAction implements ModelDriven<Attachment>, Preparable {
    
    private int fileId;
    private Attachment model;
    private AttachmentService service;
    
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
    
    public void prepare() throws Exception {
        model = service.getObject(fileId);
    } 
    
    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }
    
    public Attachment getAttachmentDetail(){
        return model;
    }
    
    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }
    
    @Override
    public String execute(){
        return SUCCESS;
    }

    public Attachment getModel() {
        return model;
    }

}
