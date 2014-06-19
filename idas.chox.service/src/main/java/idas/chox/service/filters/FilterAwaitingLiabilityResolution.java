package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;
import idas.chox.core.search.ClaimSearchCriteria;

public class FilterAwaitingLiabilityResolution extends BaseFilter {
    private String status;
    private String name;
    private String key;


    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
  
        if (isCHO == null) { // CHOX Admin
            claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
            claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
        } else if (isCHO) {
            claimSearchCriteria.setFinalReviewCho(Boolean.FALSE);
        } else {
            claimSearchCriteria.setFinalReviewIns(Boolean.FALSE);
        }

        return claimSearchCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}
