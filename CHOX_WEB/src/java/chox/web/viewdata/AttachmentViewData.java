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
    private String fileName;
    private String category;
    private String remarks;
    
    public AttachmentViewData(Attachment attachment)
    {
        this.fileName = attachment.getFileName();
        this.category = attachment.getCategory();
        this.remarks = attachment.getRemarks();
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
