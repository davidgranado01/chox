package idas.chox.service.filters;

import idas.chox.core.enums.FinalReviewMapping;
import idas.chox.core.search.ClaimSearchCriteria;
import java.util.Arrays;
import java.util.HashSet;

public class AwaitingLiabilityResolution extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        if (super.securityInfoProvider.getCurrentUser().isCHOXAdmin()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.CHO_AND_INS_FALSE.getValue());
        } else if (super.securityInfoProvider.getCurrentUser().isCHO()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.CHO_FALSE.getValue());
        } else if (super.securityInfoProvider.getCurrentUser().isAnInsurer()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.INS_FALSE.getValue());
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
