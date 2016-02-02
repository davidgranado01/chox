package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;

public interface LookupService {

    List<LookupItem> getStatuses(boolean isWorkgroupEnabled, boolean isClaimOwnershipEnabled,
                            boolean isFnolEnabled, boolean isEngineersEnabled,
                            boolean isTpiEnabled, boolean isManualInvoiceAllowed,
                            boolean isSubscriberActivated);
    
    List<LookupItem> getLiabilityStatuses(boolean withNull);
    
    List<LookupItem> getClaimTypes(WebUser user);
    List<LookupItem> getClaimTypes(Insurer insurer);
    
    List<LookupItem> getAutomaticRoutingStrategies();

    List getVehicleClasses();

    String getVehicleClassName(int id);
    
    List<ReasonOfRejection> getClaimRejectionReason(int insurerId, ClaimType claimType);
    
    List<ReasonOfRejection> getClaimClosureReason(int insurerId, ClaimType claimType);

    List<ReasonOfRejection> getClaimRejectionRestrictedReason(int insurerId, ClaimType claimType);
    
    List<ReasonOfRejection> getInvoiceRejectionReason(int insurerId, ClaimType claimType);

    List getNonProvisionReason();

    List getAllSuppliers();

    List getSuppliers(Integer insurerId, boolean excludeManualCHO);

    List<Chorganisation> getSuppliers(boolean excludeManualCHO);

    List getInsurers();

    List getInsurers(Integer choId);

    List getAllInsurers();

    List getInsurerChoBand(int insurerId);

    List getReasonOfDelay();

    List getWorkgroups(WebUser user, boolean isActiveOnly);

    List getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly);
    
    List getWorkgroupsByClaimId(int claimId, boolean isActiveOnly);

    List getSitesByInsurerId(int insurerId, boolean isActiveOnly);

    List getTeamsBySite(int insurerId, String site, boolean isActiveOnly);
    
    List getFinalReviewValues();
}
