package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class History implements Serializable
{
	/** 
	 * This attribute maps to the column id in the history table.
	 */
	protected int id;
        protected String ruleId;
        protected boolean isSystem;
	/** 
	 * This attribute maps to the column created_by in the history table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column created_date in the history table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the history table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the history table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column claim_id in the history table.
	 */
	protected int claimId;

	/** 
	 * This attribute maps to the column narrative in the history table.
	 */
	protected String narrative;

	/** 
	 * This attribute maps to the column process_date in the history table.
	 */
	protected Date processDate;

	/** 
	 * This attribute maps to the column is_public in the history table.
	 */
	protected boolean isPublic;

	/** 
	 * This attribute maps to the column type in the history table.
	 */
	protected String type;

	/**
	 * Method 'History'
	 * 
	 */
	public History()
	{
	}

	/**
	 * Method 'getId'
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Method 'getCreatedBy'
	 * 
	 * @return int
	 */
	public int getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Method 'setCreatedBy'
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Method 'getCreatedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * Method 'setCreatedDate'
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * Method 'getLastModifiedBy'
	 * 
	 * @return int
	 */
	public int getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * Method 'setLastModifiedBy'
	 * 
	 * @param lastModifiedBy
	 */
	public void setLastModifiedBy(int lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * Method 'getLastModifiedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	/**
	 * Method 'setLastModifiedDate'
	 * 
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(java.util.Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Method 'getClaimId'
	 * 
	 * @return int
	 */
	public int getClaimId()
	{
		return claimId;
	}

	/**
	 * Method 'setClaimId'
	 * 
	 * @param claimId
	 */
	public void setClaimId(int claimId)
	{
		this.claimId = claimId;
	}

	/**
	 * Method 'getNarrative'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNarrative()
	{
		return narrative;
	}

	/**
	 * Method 'setNarrative'
	 * 
	 * @param narrative
	 */
	public void setNarrative(java.lang.String narrative)
	{
		this.narrative = narrative;
	}

	/**
	 * Method 'getProcessDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getProcessDate()
	{
		return processDate;
	}

	/**
	 * Method 'setProcessDate'
	 * 
	 * @param processDate
	 */
	public void setProcessDate(java.util.Date processDate)
	{
		this.processDate = processDate;
	}

	/**
	 * Method 'getIsPublic'
	 * 
	 * @return short
	 */
	public boolean getIsPublic()
	{
		return isPublic;
	}

	/**
	 * Method 'setIsPublic'
	 * 
	 * @param isPublic
	 */
	public void setIsPublic(boolean isPublic)
	{
		this.isPublic = isPublic;
	}

	/**
	 * Method 'getType'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getType()
	{
		return type;
	}

	/**
	 * Method 'setType'
	 * 
	 * @param type
	 */
	public void setType(java.lang.String type)
	{
		this.type = type;
	}

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public boolean isIsSystem() {
            return isSystem;
        }

        public void setIsSystem(boolean isSystem) {
            this.isSystem = isSystem;
        }

}
