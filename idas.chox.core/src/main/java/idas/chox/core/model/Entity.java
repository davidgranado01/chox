package idas.chox.core.model;

import java.util.Date;

/**
 *
 * @author emmanuel
 */
public class Entity implements Auditable, Versioned {

    private Integer id;
    private Integer version;
    private WebUser createdBy;
    private Date createdDate;
    private WebUser lastModifiedBy;
    private Date lastModifiedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isTransient() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null ||
                !(o instanceof Entity)) {

            return false;
        }

        Entity other = (Entity) o;

        // if the id is missing, return false
        if (id == null) {
            return false;
        }

        // equivalence by id
        return id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return super.hashCode();
        }
    }
}
