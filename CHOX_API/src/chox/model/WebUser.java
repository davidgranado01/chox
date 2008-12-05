package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class WebUser implements Serializable,Auditable
{
	/** 
	 * This attribute maps to the column id in the web_user table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column email in the web_user table.
	 */
	protected String email;

	/** 
	 * This attribute maps to the column first_name in the web_user table.
	 */
	protected String firstName;

	/** 
	 * This attribute maps to the column last_name in the web_user table.
	 */
	protected String lastName;

	/** 
	 * This attribute maps to the column password in the web_user table.
	 */
	protected String password;

	/** 
	 * This attribute maps to the column status in the web_user table.
	 */
	protected int status;

	/** 
	 * This attribute maps to the column created_by in the web_user table.
	 */
	protected WebUser createdBy;

	/** 
	 * This attribute maps to the column created_date in the web_user table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the web_user table.
	 */
	protected WebUser lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the web_user table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the chorganisation table.
	 */
	protected Chorganisation chorganisation;

	/** 
	 * This attribute represents the foreign key relationship to the insurer table.
	 */
	protected Insurer insurer;

	/** 
	 * This attribute represents the foreign key relationship to the web_user_role table.
	 */
	protected WebUserRole webUserRole;
        
        protected Set roles;

	/**
	 * Method 'WebUser'
	 * 
	 */
	public WebUser()
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
	 * Method 'getEmail'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEmail()
	{
		return email;
	}

	/**
	 * Method 'setEmail'
	 * 
	 * @param email
	 */
	public void setEmail(java.lang.String email)
	{
		this.email = email;
	}

	/**
	 * Method 'getFirstName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFirstName()
	{
		return firstName;
	}

	/**
	 * Method 'setFirstName'
	 * 
	 * @param firstName
	 */
	public void setFirstName(java.lang.String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * Method 'getLastName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLastName()
	{
		return lastName;
	}

	/**
	 * Method 'setLastName'
	 * 
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * Method 'getPassword'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPassword()
	{
		return password;
	}

	/**
	 * Method 'setPassword'
	 * 
	 * @param password
	 */
	public void setPassword(java.lang.String password)
	{
		this.password = password;
	}

	/**
	 * Method 'getStatus'
	 * 
	 * @return int
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * Method 'setStatus'
	 * 
	 * @param status
	 */
	public void setStatus(int status)
	{
		this.status = status;
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
	 * Method 'getChorganisation'
	 * 
	 * @return Chorganisation
	 */
	public Chorganisation getChorganisation()
	{
		return chorganisation;
	}

	/**
	 * Method 'setChorganisation'
	 * 
	 * @param chorganisation
	 */
	public void setChorganisation(Chorganisation chorganisation)
	{
		this.chorganisation = chorganisation;
	}

	/**
	 * Method 'getInsurer'
	 * 
	 * @return Insurer
	 */
	public Insurer getInsurer()
	{
		return insurer;
	}

	/**
	 * Method 'setInsurer'
	 * 
	 * @param insurer
	 */
	public void setInsurer(Insurer insurer)
	{
		this.insurer = insurer;
	}
        
        public void setRoles(Set roles)
        {
            this.roles = roles;
        }
        
        public Set getRoles()
        {
            return this.roles;
        }


}
