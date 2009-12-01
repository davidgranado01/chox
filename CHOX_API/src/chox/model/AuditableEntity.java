/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import java.util.Date;

/**
 *
 * @author emmanuel
 */
public class AuditableEntity implements Auditable {

    protected Integer id;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WebUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public WebUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(WebUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(java.util.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
