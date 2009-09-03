package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IInsurerInfo;

public class Insurer implements Serializable, IInsurerInfo,Auditable
{
	protected int id;
	protected String name;
	protected WebUser createdBy;
	protected BigDecimal adminHandlingCharge;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;
        protected boolean status;
        protected String address1;
	protected String address2;
	protected String address3;
	protected String address4;
	protected String address5;
	protected String postcode;
	protected String vatNo;
	protected String companyNo;
        protected String phone;
        protected boolean workgroupEnable;
        
	public Insurer()
	{
	}

        public BigDecimal getAdminHandlingCharge() {
            return adminHandlingCharge;
        }

        public void setAdminHandlingCharge(BigDecimal adminHandlingCharge) {
            this.adminHandlingCharge = adminHandlingCharge;
        }

	public int getId()
	{
            return id;
	}

	public void setId(int id)
	{
            this.id = id;
	}

	public java.lang.String getName()
	{
            return name;
	}

	public void setName(java.lang.String name)
	{
            this.name = name;
	}

	public WebUser getCreatedBy()
	{
            return createdBy;
	}

	public void setCreatedBy(WebUser createdBy)
	{
            this.createdBy = createdBy;
	}

	public java.util.Date getCreatedDate()
	{
            return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate)
	{
            this.createdDate = createdDate;
	}

	public WebUser getLastModifiedBy()
	{
            return lastModifiedBy;
	}

	public void setLastModifiedBy(WebUser lastModifiedBy)
	{
            this.lastModifiedBy = lastModifiedBy;
	}

	public java.util.Date getLastModifiedDate()
	{
            return lastModifiedDate;
	}

	public void setLastModifiedDate(java.util.Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getAddress4() {
            return address4;
        }

        public void setAddress4(String address4) {
            this.address4 = address4;
        }

        public String getAddress5() {
            return address5;
        }

        public void setAddress5(String address5) {
            this.address5 = address5;
        }

        public String getCompanyNo() {
            return companyNo;
        }

        public void setCompanyNo(String companyNo) {
            this.companyNo = companyNo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getVatNo() {
            return vatNo;
        }

        public void setVatNo(String vatNo) {
            this.vatNo = vatNo;
        }

        public boolean isWorkgroupEnable() {
            return workgroupEnable;
        }

        public void setWorkgroupEnable(boolean workgroupEnable) {
            this.workgroupEnable = workgroupEnable;
        }

}
