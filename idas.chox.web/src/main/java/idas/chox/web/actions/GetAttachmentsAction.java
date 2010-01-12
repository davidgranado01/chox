package idas.chox.web.actions;

import idas.chox.core.model.Attachment;
import idas.chox.core.services.AttachmentService;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.AttachmentViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class GetAttachmentsAction extends BaseAction {

    private AttachmentService service;
    private List<AttachmentViewData> attachments;
    private Integer claimId;

    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.attachments);
        return "{totalCount:" + this.attachments.size() + ",results:" + jObject.toString() + "}";
    }

    String getTabName() {
        return ApplicationAccessibility.TAB_PAYMENT_PACK;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getAttachments() {
        attachments = new ArrayList<AttachmentViewData>();
        List<Attachment> AttachmentData = this.service.getAttachmentsByClaim(claimId);
        for (Attachment a : AttachmentData) {
            attachments.add(new AttachmentViewData(a));
        }
        return SUCCESS;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }
}
