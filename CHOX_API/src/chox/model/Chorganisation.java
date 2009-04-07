package chox.model;

import chox.Util.TextHelper;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;
import scsbre.model.ICHOrganisationInfo;

public class Chorganisation implements Serializable,Auditable, ICHOrganisationInfo
{
	/** 
	 * This attribute maps to the column id in the chorganisation table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column name in the chorganisation table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column address1 in the chorganisation table.
	 */
	protected String address1;

	/** 
	 * This attribute maps to the column address2 in the chorganisation table.
	 */
	protected String address2;

	/** 
	 * This attribute maps to the column address3 in the chorganisation table.
	 */
	protected String address3;

	/** 
	 * This attribute maps to the column address4 in the chorganisation table.
	 */
	protected String address4;

	/** 
	 * This attribute maps to the column address5 in the chorganisation table.
	 */
	protected String address5;

	/** 
	 * This attribute maps to the column postcode in the chorganisation table.
	 */
	protected String postcode;

	/** 
	 * This attribute maps to the column vat_no in the chorganisation table.
	 */
	protected String vatNo;

	/** 
	 * This attribute maps to the column company_no in the chorganisation table.
	 */
	protected String companyNo;

	/** 
	 * This attribute maps to the column is_delegated_authority in the chorganisation table.
	 */
	protected boolean isDelegatedAuthority;

	/** 
	 * This attribute maps to the column created_by in the chorganisation table.
	 */
	protected WebUser createdBy;

	/** 
	 * This attribute maps to the column created_date in the chorganisation table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the chorganisation table.
	 */
	protected WebUser lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the chorganisation table.
	 */
	protected Date lastModifiedDate;
	/**
	 * Method 'Chorganisation'
	 * 
	 */
	public Chorganisation()
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
	 * Method 'getName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName()
	{
		return name;
	}

	/**
	 * Method 'setName'
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	/**
	 * Method 'getAddress1'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress1()
	{
		return address1;
	}

	/**
	 * Method 'setAddress1'
	 * 
	 * @param address1
	 */
	public void setAddress1(java.lang.String address1)
	{
		this.address1 = address1;
	}

	/**
	 * Method 'getAddress2'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress2()
	{
		return address2;
	}

	/**
	 * Method 'setAddress2'
	 * 
	 * @param address2
	 */
	public void setAddress2(java.lang.String address2)
	{
		this.address2 = address2;
	}

	/**
	 * Method 'getAddress3'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress3()
	{
		return address3;
	}

	/**
	 * Method 'setAddress3'
	 * 
	 * @param address3
	 */
	public void setAddress3(java.lang.String address3)
	{
		this.address3 = address3;
	}

	/**
	 * Method 'getAddress4'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress4()
	{
		return address4;
	}

	/**
	 * Method 'setAddress4'
	 * 
	 * @param address4
	 */
	public void setAddress4(java.lang.String address4)
	{
		this.address4 = address4;
	}

	/**
	 * Method 'getAddress5'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress5()
	{
		return address5;
	}

	/**
	 * Method 'setAddress5'
	 * 
	 * @param address5
	 */
	public void setAddress5(java.lang.String address5)
	{
		this.address5 = address5;
	}

	/**
	 * Method 'getPostcode'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPostcode()
	{
		return postcode;
	}

	/**
	 * Method 'setPostcode'
	 * 
	 * @param postcode
	 */
	public void setPostcode(java.lang.String postcode)
	{
		this.postcode = postcode;
	}

	/**
	 * Method 'getVatNo'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVatNo()
	{
		return vatNo;
	}

	/**
	 * Method 'setVatNo'
	 * 
	 * @param vatNo
	 */
	public void setVatNo(java.lang.String vatNo)
	{
		this.vatNo = vatNo;
	}

	/**
	 * Method 'getCompanyNo'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCompanyNo()
	{
		return companyNo;
	}

	/**
	 * Method 'setCompanyNo'
	 * 
	 * @param companyNo
	 */
	public void setCompanyNo(java.lang.String companyNo)
	{
		this.companyNo = companyNo;
	}

	/**
	 * Method 'isIsDelegatedAuthority'
	 * 
	 * @return boolean
	 */
	public boolean isIsDelegatedAuthority()
	{
		return isDelegatedAuthority;
	}

	/**
	 * Method 'setIsDelegatedAuthority'
	 * 
	 * @param isDelegatedAuthority
	 */
	public void setIsDelegatedAuthority(boolean isDelegatedAuthority)
	{
		this.isDelegatedAuthority = isDelegatedAuthority;
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

        public boolean getIsDelegatedAuthority() {
            return this.isDelegatedAuthority;
        }
        
        public String getDisplayAddress(){
            
            String strDelimiter = ",";
            
            System.out.println(">>>address1:"+address1+"|"+TextHelper.appendDelimiter(address1, strDelimiter));
            System.out.println(">>>address2:"+address2+"|"+TextHelper.appendDelimiter(address2, strDelimiter));
            System.out.println(">>>address3:"+address3+"|"+TextHelper.appendDelimiter(address3, strDelimiter));
            System.out.println(">>>address4:"+address4+"|"+TextHelper.appendDelimiter(address4, strDelimiter));
            System.out.println(">>>address5:"+address5+"|"+TextHelper.appendDelimiter(address5, strDelimiter));
            System.out.println(">>>postcode:"+postcode+"|"+TextHelper.appendDelimiter(postcode, strDelimiter));
            
            /*
            System.out.println(">>>>"+address1 + TextHelper.appendDelimiter(address2, strDelimiter) + TextHelper.appendDelimiter(address3, strDelimiter) + TextHelper.appendDelimiter(address4, strDelimiter) 
                    + TextHelper.appendDelimiter(postcode, strDelimiter) + TextHelper.appendDelimiter(address5, ""));
            */
            return "ABC";
            //return "A"+address1 + TextHelper.appendDelimiter(address2, strDelimiter) + TextHelper.appendDelimiter(address3, strDelimiter) + TextHelper.appendDelimiter(address4, strDelimiter) 
            //        + TextHelper.appendDelimiter(postcode, strDelimiter) + TextHelper.appendDelimiter(address5, "");
        }
}
