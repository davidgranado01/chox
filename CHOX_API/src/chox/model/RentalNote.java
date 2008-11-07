/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import com.filesystemsoftware.utils.StringEncoder;
import java.sql.Timestamp;

public class RentalNote {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID = -1;
    private String note;
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private long sessionID = -1;
    
    private String filename=null;
    private String fileMIMEType=null;
    private byte [] fileBLOB=null;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public byte[] getFileBLOB() {
        return fileBLOB;
    }

    public void setFileBLOB(byte[] fileBLOB) {
        this.fileBLOB = fileBLOB;
    }

    public String getFileMIMEType() {
        return fileMIMEType;
    }

    public void setFileMIMEType(String fileMIMEType) {
        this.fileMIMEType = fileMIMEType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
}
