/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.model.Attachment;

/**
 *
 * @author Carlson
 */
public class AttachmentViewData {
    private int id;
    private String fileName;
    private String category;
    private String remarks;
    
    public AttachmentViewData(Attachment attachment)
    {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();
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
