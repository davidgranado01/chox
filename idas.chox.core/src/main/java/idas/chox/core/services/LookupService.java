package idas.chox.core.services;

import idas.chox.core.model.WebUser;
import java.util.List;

public interface LookupService {

    public List getStatuses();
    
    public List getLiabilityStatuses();

    public List getVehicleClasses();

    public List getClaimRejectionReason();

    public List getClaimRejectionRestrictedReason();

    public List getInvoiceRejectionReason();

    public List getNonProvisionReason();

    public List getAllSuppliers();

    public List getSuppliers(Integer insurerId);

    public List getInsurers();

    public List getInsurers(Integer choId);

    public List getAllInsurers();

    public List getInsurerChoBand(int insurerId);

    public List getReasonOfDelay();

    public List getWorkgroups(WebUser user, boolean isActiveOnly);

    public List getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly);
    
    public List getWorkgroupsByClaimId(int claimId, boolean isActiveOnly);
}
