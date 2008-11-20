package scsbre.sample;

import java.util.Date;

import scsbre.model.ICHOBandInfo;
import scsbre.model.ICHOrganisationInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;
import scsbre.model.IExtrasInfo;
import scsbre.model.IHireInfo;
import scsbre.model.IInsurerInfo;
import scsbre.model.IInvoiceInfo;
import scsbre.model.IVehicleClassInfo;

public class ClaimInfo implements IClaimInfo {

    private ICHOBandInfo claimChoBand;
    private ICHOrganisationInfo claimCHOrganisation;
    private IInvoiceInfo claimInvoice;
    private IExtrasInfo claimExtras;
    private IHireInfo claimHireDetail;
    private ICustomerVehicleDamageInfo claimCustomerVehicleDamage;
    private IInsurerInfo claimInsurer;
    private IEngineerReportInfo claimEngineeringReport;
    private boolean managingRepair;
    private Date policyHolderContactDate;
    private IVehicleClassInfo vClass;


    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimChoBand()
     */
    public ICHOBandInfo getClaimChoBand() {
        return claimChoBand;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimChoBand(scsbre.model.CHOBandInfo)
     */

    public void setClaimChoBand(ICHOBandInfo claimChoBand) {
        this.claimChoBand = claimChoBand;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimCHOrganisation()
     */

    public ICHOrganisationInfo getClaimCHOrganisation() {
        return claimCHOrganisation;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimCHOrganisation(scsbre.model.CHOrganisationInfo)
     */

    public void setClaimCHOrganisation(ICHOrganisationInfo claimCHOrganisation) {
        this.claimCHOrganisation = claimCHOrganisation;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimInvoice()
     */

    public IInvoiceInfo getClaimInvoice() {
        return claimInvoice;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimInvoice(scsbre.model.InvoiceInfo)
     */

    public void setClaimInvoice(IInvoiceInfo claimInvoice) {
        this.claimInvoice = claimInvoice;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimExtras()
     */

    public IExtrasInfo getClaimExtras() {
        return claimExtras;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimExtras(scsbre.model.ExtrasInfo)
     */

    public void setClaimExtras(IExtrasInfo claimExtras) {
        this.claimExtras = claimExtras;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimHireDetail()
     */

    public IHireInfo getClaimHireDetail() {
        return claimHireDetail;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimHireDetail(scsbre.model.HireInfo)
     */

    public void setClaimHireDetail(IHireInfo claimHireDetail) {
        this.claimHireDetail = claimHireDetail;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimCustomerVehicleDamage()
     */

    public ICustomerVehicleDamageInfo getClaimCustomerVehicleDamage() {
        return claimCustomerVehicleDamage;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimCustomerVehicleDamage(scsbre.model.CustomerVehicleDamageInfo)
     */

    public void setClaimCustomerVehicleDamage(
            ICustomerVehicleDamageInfo claimCustomerVehicleDamage) {
        this.claimCustomerVehicleDamage = claimCustomerVehicleDamage;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimInsurer()
     */

    public IInsurerInfo getClaimInsurer() {
        return claimInsurer;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimInsurer(scsbre.model.InsurerInfo)
     */

    public void setClaimInsurer(IInsurerInfo claimInsurer) {
        this.claimInsurer = claimInsurer;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getClaimEngineeringReport()
     */

    public IEngineerReportInfo getClaimEngineeringReport() {
        return claimEngineeringReport;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setClaimEngineeringReport(scsbre.model.EngineerReportInfo)
     */

    public void setClaimEngineeringReport(IEngineerReportInfo claimEngineeringReport) {
        this.claimEngineeringReport = claimEngineeringReport;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#getManagingRepair()
     */

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

    public IVehicleClassInfo getVClass() {
        return vClass;
    }
    /* (non-Javadoc)
     * @see scsbre.model.IClaimInfo#setVClass(scsbre.model.VehicleClassInfo)
     */

    public void setVClass(IVehicleClassInfo class1) {
        vClass = class1;
    }
}
