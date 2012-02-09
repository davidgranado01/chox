package idas.chox.web.viewdata;

import idas.chox.core.model.Attachment;
import idas.chox.core.util.DateHelper;

public class AttachmentViewData {

    private int id;
    private String fileName;
    private String category;
    private String remarks;
    private String modifiedDate;
    private String delete = "Delete";

    public AttachmentViewData(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();
        this.modifiedDate = DateHelper.getLocalDateTimeFormat().format(attachment.getLastModifiedDate());
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

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getRemarks() {
        return remarks;
    }
}
