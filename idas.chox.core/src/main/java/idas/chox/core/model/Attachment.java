package idas.chox.core.model;

import java.io.Serializable;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class Attachment extends Entity implements Serializable {

    private String fileName;
    private String remarks;
    private String category;
    private Claim claim;
    private String fileType;
    private AttachmentFile attachment;
    private boolean deleted;

    public String getCategory() {
        return StringEscapeUtils.unescapeHtml4(Jsoup.clean(category, Whitelist.none()));
    }

    public void setCategory(String category) {
        this.category = StringEscapeUtils.unescapeHtml4(Jsoup.clean(category, Whitelist.none()));
    }

    public AttachmentFile getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentFile attachment) {
        this.attachment = attachment;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = StringEscapeUtils.unescapeHtml4(Jsoup.clean(remarks, Whitelist.none()));
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
