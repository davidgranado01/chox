package chox.model;

import java.util.Date;
import chox.model.Insurer;

public class InsurerAllias {
    protected int id;
    protected String alliasName;
    protected int createdBy;
    protected Date createdDate;
    protected int lastModifiedBy;
    protected Date lastModifiedDate;
    protected Insurer insurer;

    public String getAlliasName() {
        return alliasName;
    }

    public void setAlliasName(String alliasName) {
        this.alliasName = alliasName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    
}
