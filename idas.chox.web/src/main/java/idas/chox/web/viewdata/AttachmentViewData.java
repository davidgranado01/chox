package idas.chox.web.viewdata;

import idas.chox.core.model.Attachment;
import idas.chox.core.util.DateHelper;

public class AttachmentViewData {

    private int id;
    private String fileName;
    private String category;
    private String remarks;
    private String createdDate;
    private String delete = "Delete";

    public AttachmentViewData(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(attachment.getCreatedDate());
//        this.createdDate = attachment.getCreatedDate();
    }

    public String getCategory() {
        return category;
    }

    public String getDelete() {
        return delete;
    }

    public String getFileName() {
        return fileName;
    }

    public int getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getRemarks() {
        return remarks;
    }
}
