package idas.chox.core.services;

import idas.chox.core.model.LookupItem;
import idas.chox.core.model.WebUser;
import java.util.List;

public interface LookupService {

    public List<LookupItem> getStatuses(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
                            boolean isFnolEnabled, boolean isEngineersEnabled,
                            boolean isTpiEnabled, boolean isManualInvoiceAllowed,
                            boolean isSubscriberActivated);
    
    public List<LookupItem> getLiabilityStatuses();
    
    public List<LookupItem> getClaimTypes();

    public List getVehicleClasses();

    public String getVehicleClassName(int id);

    public List getClaimRejectionReason();

    public List getClaimRejectionRestrictedReason();

    public List getInvoiceRejectionReason();

    public List getNonProvisionReason();

    public List getAllSuppliers();

    public List getSuppliers(Integer insurerId);

    public List getSuppliers();

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
