/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.model;

import java.util.Date;

/**
 *
 * @author emmanuel
 */
public class Entity implements Auditable {

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

    public boolean isTransient()
    {
        return id == null || id <= 0;
    }

    @Override
    public WebUser getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public WebUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(WebUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(java.util.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
