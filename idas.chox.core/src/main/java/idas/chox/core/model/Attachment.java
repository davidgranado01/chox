package idas.chox.core.model;

import java.io.Serializable;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class Attachment extends Entity implements Serializable {

    private String fileName;
    private String remarks;
    private String category;
    private Claim claim;
    private String fileType;
    private byte[] fileBuffer;
    private boolean deleted;

    public byte[] getFileBuffer() {
        if (fileBuffer == null)
            return null;
        
        return Arrays.copyOf(fileBuffer, fileBuffer.length);
    }

    public void setFileBuffer(byte[] fb) {
        if (fb == null) {
            fileBuffer = null;
        } else {
            fileBuffer = Arrays.copyOf(fb, fb.length); 
        }
    }

    public String getCategory() {
        return Jsoup.clean(category, Whitelist.none());
    }

    public void setCategory(String category) {
        this.category = Jsoup.clean(category, Whitelist.none());
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
        this.remarks = Jsoup.clean(remarks, Whitelist.none());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
