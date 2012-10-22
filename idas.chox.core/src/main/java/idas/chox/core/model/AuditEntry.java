package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author John
 */
public class AuditEntry implements Serializable {
    private Integer id;
    private String tableName;
    private Integer objectId;
    private String parameterName;
    private String oldValue;
    private String newValue;
    private Date createdDate;
    private WebUser createdBy;

    public AuditEntry(){}

    public AuditEntry(String table, int id, String parameter, String oldValue, String newValue, WebUser user) {
        tableName = table;
        objectId = id;
        parameterName = parameter;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.createdBy = user;
        createdDate = new Date();
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public WebUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
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
