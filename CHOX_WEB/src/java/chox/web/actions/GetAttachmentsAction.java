/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Attachment;
import chox.services.AttachmentService;
import chox.web.security.ApplicationAccessibility;
import chox.web.viewdata.AttachmentViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;


public class GetAttachmentsAction extends BaseModelAction {

    private int claimId;
    private AttachmentService service;
    private List<AttachmentViewData> attachments;

    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }
       
   public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.attachments);
        return "{totalCount:" + this.attachments.size() + ",results:" + jObject.toString() + "}";
    }
   
    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_PAYMENT_PACK;
    }
    
    @Override
    public String execute()
    {        
        return SUCCESS;
    }
    
    public String getAttachments()
    {        
        attachments = new ArrayList<AttachmentViewData>();
        List<Attachment> attachmwentData = this.service.getAttachmentByClaimId(claimId);
        
        for(Attachment a : attachmwentData)
        {
            attachments.add(new AttachmentViewData(a));
        }
        
        return SUCCESS;
    }
    
    public

    int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }    
    
}
