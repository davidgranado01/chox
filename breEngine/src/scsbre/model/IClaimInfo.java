package scsbre.model;

import java.util.Date;

public interface IClaimInfo {

    public ICHOBandInfo getChoBand();

    public ICHOrganisationInfo getCHOrg();

    public IInvoiceInfo getInvoice();

    public IExtrasInfo getExtras();

    public IHireInfo getHireDetail();

    public ICustomerVehicleDamageInfo getCustomerVehicleDamage();

    public IInsurerInfo getInsurer();

    public IEngineerReportInfo getEngineeringReport();

    public boolean getManagingRepair();

    public Date getPolicyHolderContactDate();

    public IVehicleClassInfo getVClass();
    
    public Date getHireMonitoringEcd();

}