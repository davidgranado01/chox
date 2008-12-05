package chox.model;

import java.io.Serializable;
import java.util.Date;

public class HireMonitoringEcd implements Serializable,Auditable
{
	/** 
	 * This attribute maps to the column created_by in the hire_monitoring_ecd table.
	 */
	protected WebUser createdBy;

	/** 
	 * This attribute maps to the column created_date in the hire_monitoring_ecd table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the hire_monitoring_ecd table.
	 */
	protected WebUser lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the hire_monitoring_ecd table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column id in the hire_monitoring_ecd table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column ecd_date in the hire_monitoring_ecd table.
	 */
	protected Date ecdDate;

	/** 
	 * This attribute maps to the column sequence in the hire_monitoring_ecd table.
	 */
	protected int sequence;

	/** 
	 * This attribute maps to the column reason_id in the hire_monitoring_ecd table.
	 */
	protected String reason;

	/** 
	 * This attribute represents the foreign key relationship to the claim table.
	 */
	protected Claim claim;
        
        private String supportingNote;
                

	/**
	 * Method 'HireMonitoringEcd'
	 * 
	 */
	public HireMonitoringEcd()
	{
	}

	/**
	 * Method 'getCreatedBy'
	 * 
	 * @return int
	 */
	public WebUser getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Method 'setCreatedBy'
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(WebUser createdBy)
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
	public WebUser getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * Method 'setLastModifiedBy'
	 * 
	 * @param lastModifiedBy
	 */
	public void setLastModifiedBy(WebUser lastModifiedBy)
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
	 * Method 'getEcdDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getEcdDate()
	{
		return ecdDate;
	}

	/**
	 * Method 'setEcdDate'
	 * 
	 * @param ecdDate
	 */
	public void setEcdDate(java.util.Date ecdDate)
	{
		this.ecdDate = ecdDate;
	}

	/**
	 * Method 'getSequence'
	 * 
	 * @return int
	 */
	public int getSequence()
	{
		return sequence;
	}

	/**
	 * Method 'setSequence'
	 * 
	 * @param sequence
	 */
	public void setSequence(int sequence)
	{
		this.sequence = sequence;
	}

	/**
	 * Method 'getReasonId'
	 * 
	 * @return int
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * Method 'setReasonId'
	 * 
	 * @param reasonId
	 */
	public void setReason(String reason)
	{
		this.reason = reason;
	}

	/**
	 * Method 'getClaim'
	 * 
	 * @return Claim
	 */
	public Claim getClaim()
	{
		return claim;
	}

	/**
	 * Method 'setClaim'
	 * 
	 * @param claim
	 */
	public void setClaim(Claim claim)
	{
		this.claim = claim;
	}

    public String getSupportingNote() {
        return supportingNote;
    }

    public void setSupportingNote(String supportingNote) {
        this.supportingNote = supportingNote;
    }

}
