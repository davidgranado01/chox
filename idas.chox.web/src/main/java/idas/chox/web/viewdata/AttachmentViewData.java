package idas.chox.web.viewdata;

import idas.chox.core.model.Attachment;
import idas.chox.core.util.DateHelper;

public class AttachmentViewData {

    private final int id;
    private final String fileName;
    private final String category;
    private final String remarks;
    private final String createdDate;
    private final String delete = "Delete";
    private final boolean removed;

    public AttachmentViewData(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(attachment.getCreatedDate());
        this.removed = attachment.isRemoved();
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
    public boolean getRemoved() {
        return removed;
    }
    public boolean isRemoved() {
        return removed;
    }
}
