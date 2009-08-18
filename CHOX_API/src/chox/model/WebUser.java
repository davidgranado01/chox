package chox.model;

import java.util.Set;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

public class WebUser implements Serializable
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
	protected boolean status;

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

    private Boolean isExpired;

	/** 
	 * This attribute represents the foreign key relationship to the web_user_role table.
	 */
	protected WebUserRole webUserRole;
        
        protected Set roles;
        protected String organisationName;
        protected LineOfBusiness lineOfBusiness;
        protected boolean claimHandler = false;

    public void setClaimHandler(boolean claimHandler) {
        this.claimHandler = claimHandler;
    }
        

	/**
	 * Method 'WebUser'
	 * 
	 */
	public WebUser()
	{
        isExpired = false;
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
	public boolean getStatus()
	{
		return status;
	}

	/**
	 * Method 'setStatus'
	 * 
	 * @param status
	 */
	public void setStatus(boolean status)
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

        public String getOrganisationName()
        {
            Chorganisation cho = this.getChorganisation();
            Insurer ins = this.getInsurer();

            if (ins != null) {
                organisationName = ins.getName();
            } else if (cho != null) {
                organisationName = cho.getName();
            }
            
            return organisationName;
        }
        
        @Override
        public String toString()
        {
            String orgName = orgName = String.format("(%1$s)", getOrganisationName());
            return String.format("%1$s %2$s %3$s", this.getFirstName(), this.getLastName(), orgName);
        }

        public boolean isCHOXAdmin(){

            boolean bFlag = false;
            
            if(this.roles.size()>0){
                Iterator itr = roles.iterator();

                while(itr.hasNext()) {
                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if(webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)){
                        bFlag = true;
                        break;
                    }
                }
            }
            
            return bFlag;
            
        }
        
        public boolean isClaimHandler(){

            boolean bFlag = false;
            
            if(this.roles.size()>0){
                Iterator itr = roles.iterator();

                while(itr.hasNext()) {
                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if(webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH)){
                        bFlag = true;
                        break;
                    }
                }
            }
            
            return bFlag;
            
        }
        
        public String getDisplayName(){
            return String.format("%1$s %2$s", this.getFirstName(), this.getLastName());
        }
        
        public int getOrganisationType(){
            
            Integer typeIndex = -1;
            
            if(this.isCHOXAdmin()){
                
                typeIndex = 1;
                
            }else{
                
                if(this.getInsurer()!=null){
                     typeIndex = 2;
                }else if(this.getChorganisation()!=null){
                    typeIndex = 3;
                }
            }
            
            return typeIndex;
        }

    public LineOfBusiness getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    /**
     * @return the isExpired
     */
    public Boolean getIsExpired() {
        return isExpired;
    }

    /**
     * @param isExpired the isExpired to set
     */
    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

}
