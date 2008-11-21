package scsbre.model;

import java.util.Date;

public interface IClaimInfo {

    public ICHOBandInfo getChoBand();

    public void setChoBand(ICHOBandInfo claimChoBand);

    public ICHOrganisationInfo getCHOrganisation();

    public void setCHOrganisation(ICHOrganisationInfo chOrg);

    public IInvoiceInfo getInvoice();

    public void setInvoice(IInvoiceInfo claimInvoice);

    public IExtrasInfo getExtras();

    public void setExtras(IExtrasInfo extras);

    public IHireInfo getHireDetail();

    public void setHireDetail(IHireInfo hireDetail);

    public ICustomerVehicleDamageInfo getCustomerVehicleDamage();

    public void setCustomerVehicleDamage(
            ICustomerVehicleDamageInfo claimCustomerVehicleDamage);

    public IInsurerInfo getInsurer();

    public void setInsurer(IInsurerInfo insurer);

    public IEngineerReportInfo getEngineeringReport();

    public void setClaimEngineeringReport(
            IEngineerReportInfo engineeringReport);

    public boolean getManagingRepair();

    public void setManagingRepair(boolean managingRepair);

    public Date getPolicyHolderContactDate();

    public void setPolicyHolderContactDate(Date policyHolderContactDate);

    public IVehicleClassInfo getVClass();

    public void setVClass(IVehicleClassInfo class1);
}