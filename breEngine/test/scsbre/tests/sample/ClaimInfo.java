package scsbre.tests.sample;

import java.util.Date;

import scsbre.model.ICHOrganisationInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IHireMonitoringDetail;
import scsbre.model.IInsurerInfo;

public class ClaimInfo implements IClaimInfo {

    private CHOBandInfo claimChoBand;
    private CHOrganisationInfo claimCHOrganisation;
    private InvoiceInfo claimInvoice;
    private ExtrasInfo claimExtras;
    private HireInfo claimHireDetail;
    private CustomerVehicleDamageInfo claimCustomerVehicleDamage;
    private InsurerInfo claimInsurer;
    private EngineerReportInfo claimEngineeringReport;
    private boolean managingRepair;
    private Date policyHolderContactDate;
    private VehicleClassInfo vClass;
    private HireMonitoringDetailInfo hireMonitoringDetail;
    private Date hireMonitoringEcd;
    private boolean isInvoiceReviewRequired;

    public CHOBandInfo getChoBand() {
        return claimChoBand;
    }

    public void setChoBand(CHOBandInfo claimChoBand) {
        this.claimChoBand = claimChoBand;
    }

    public CHOrganisationInfo getCHOrg() {
        return claimCHOrganisation;
    }

    public void setCHOrganisation(CHOrganisationInfo claimCHOrganisation) {
        this.claimCHOrganisation = claimCHOrganisation;
    }

    public InvoiceInfo getInvoice() {
        return claimInvoice;
    }

    public void setInvoice(InvoiceInfo claimInvoice) {
        this.claimInvoice = claimInvoice;
    }

    public ExtrasInfo getExtras() {
        return claimExtras;
    }

    public void setExtras(ExtrasInfo claimExtras) {
        this.claimExtras = claimExtras;
    }

    public HireInfo getHireDetail() {
        return claimHireDetail;
    }

    public void setHireDetail(HireInfo claimHireDetail) {
        this.claimHireDetail = claimHireDetail;
    }

    public CustomerVehicleDamageInfo getCustomerVehicleDamage() {
        return claimCustomerVehicleDamage;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimCustomerVehicleDamage(scsbre.model.CustomerVehicleDamageInfo)
     */

    public void setCustomerVehicleDamage(
            CustomerVehicleDamageInfo claimCustomerVehicleDamage) {
        this.claimCustomerVehicleDamage = claimCustomerVehicleDamage;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimInsurer()
     */

    public InsurerInfo getInsurer() {
        return claimInsurer;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimInsurer(scsbre.model.InsurerInfo)
     */

    public void setInsurer(InsurerInfo claimInsurer) {
        this.claimInsurer = claimInsurer;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimEngineeringReport()
     */

    public EngineerReportInfo getEngineeringReport() {
        return claimEngineeringReport;
    }
    
    public void setClaimEngineeringReport(EngineerReportInfo claimEngineeringReport) {
        this.claimEngineeringReport = claimEngineeringReport;
    }
    
    public boolean getManagingRepair() {
        return managingRepair;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setManagingRepair(boolean)
     */

    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getPolicyHolderContactDate()
     */

    public Date getPolicyHolderContactDate() {
        return policyHolderContactDate;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setPolicyHolderContactDate(java.util.Date)
     */

    public void setPolicyHolderContactDate(Date policyHolderContactDate) {
        this.policyHolderContactDate = policyHolderContactDate;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getVClass()
     */

    public VehicleClassInfo getVClass() {
        return vClass;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setVClass(scsbre.model.VehicleClassInfo)
     */

    public void setVClass(VehicleClassInfo class1) {
        vClass = class1;
    }

    public Date getHireMonitoringEcd() {
        return hireMonitoringEcd;
    }

    public void setHireMonitoringEcd(Date hireMonitoringEcd) {
        this.hireMonitoringEcd = hireMonitoringEcd;
    }

    public void setHireMonitoringDetail(HireMonitoringDetailInfo hireMonitoringDetail) {
        this.hireMonitoringDetail = hireMonitoringDetail;
    }

    public HireMonitoringDetailInfo getHireMonitoringDetail() {
        return hireMonitoringDetail;
    }

    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired =  isInvoiceReviewRequired;
    }
    
    public boolean getIsInvoiceReviewRequired() {
        return isInvoiceReviewRequired;
    }
}
