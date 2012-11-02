package idas.chox.core.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.WebUser;
import java.util.List;

public interface LookupService {

    public List<LookupItem> getStatuses(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
                            boolean isFnolEnabled, boolean isEngineersEnabled,
                            boolean isTpiEnabled, boolean isManualInvoiceAllowed,
                            boolean isSubscriberActivated);
    
    public List<LookupItem> getLiabilityStatuses(boolean withNull);
    
    public List<LookupItem> getClaimTypes();

    public List getVehicleClasses();

    public String getVehicleClassName(int id);
    
    public List getClaimRejectionReason(int insurerId, String claimType);

    public List getClaimRejectionRestrictedReason(int insurerId, String claimType);
    
    public List getInvoiceRejectionReason(int insurerId, String claimType);

    public List getNonProvisionReason();

    public List getAllSuppliers();

    public List getSuppliers(Integer insurerId, boolean excludeManualCHO);

    public List<Chorganisation> getSuppliers(boolean excludeManualCHO);

    public List getInsurers();

    public List getInsurers(Integer choId);

    public List getAllInsurers();

    public List getInsurerChoBand(int insurerId);

    public List getReasonOfDelay();

    public List getWorkgroups(WebUser user, boolean isActiveOnly);

    public List getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly);
    
    public List getWorkgroupsByClaimId(int claimId, boolean isActiveOnly);

    public List getSitesByInsurerId(int insurerId, boolean isActiveOnly);

    public List getTeamsBySite(int insurerId, String site, boolean isActiveOnly);
}
