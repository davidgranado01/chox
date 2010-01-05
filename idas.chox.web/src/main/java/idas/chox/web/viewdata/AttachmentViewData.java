/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.viewdata;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author Carlson
 */
public class AttachmentViewData {

    private int id;
    private String fileName;
    private String category;
    private String remarks;
    private String modifiedDate;
    private String modifiedBy;
    private String delete = "Delete";

    public AttachmentViewData(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();

        this.modifiedDate = DateHelper.LocalDateTimeFormat.format(attachment.getLastModifiedDate());

        WebUser user = attachment.getLastModifiedBy();
        if (user != null) {
            this.modifiedBy = String.format("%1$s %2$s", user.getFirstName(), user.getLastName());
        }

    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDelete() {
        return delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCategory() {
        return category;
    }

    public String getRemarks() {
        return remarks;
    }
}
