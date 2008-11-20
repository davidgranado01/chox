package scsbre.model;

import java.util.Date;

public interface IClaimInfo {

    public ICHOBandInfo getClaimChoBand();

    public void setClaimChoBand(ICHOBandInfo claimChoBand);

    public ICHOrganisationInfo getClaimCHOrganisation();

    public void setClaimCHOrganisation(ICHOrganisationInfo claimCHOrganisation);

    public IInvoiceInfo getClaimInvoice();

    public void setClaimInvoice(IInvoiceInfo claimInvoice);

    public IExtrasInfo getClaimExtras();

    public void setClaimExtras(IExtrasInfo claimExtras);

    public IHireInfo getClaimHireDetail();

    public void setClaimHireDetail(IHireInfo claimHireDetail);

    public ICustomerVehicleDamageInfo getClaimCustomerVehicleDamage();

    public void setClaimCustomerVehicleDamage(
            ICustomerVehicleDamageInfo claimCustomerVehicleDamage);

    public IInsurerInfo getClaimInsurer();

    public void setClaimInsurer(IInsurerInfo claimInsurer);

    public IEngineerReportInfo getClaimEngineeringReport();

    public void setClaimEngineeringReport(
            IEngineerReportInfo claimEngineeringReport);

    public boolean getManagingRepair();

    public void setManagingRepair(boolean managingRepair);

    public Date getPolicyHolderContactDate();

    public void setPolicyHolderContactDate(Date policyHolderContactDate);

    public IVehicleClassInfo getVClass();

    public void setVClass(IVehicleClassInfo class1);
}