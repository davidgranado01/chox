/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.model;

/**
 *
 * @author Emmanuel
 */
public interface Auditable {
    
    	WebUser getCreatedBy();
	void setCreatedBy(WebUser createdBy);
	java.util.Date getCreatedDate();
	void setCreatedDate(java.util.Date createdDate);
	WebUser getLastModifiedBy();
	void setLastModifiedBy(WebUser lastModifiedBy);
	java.util.Date getLastModifiedDate();
	void setLastModifiedDate(java.util.Date lastModifiedDate);

}
