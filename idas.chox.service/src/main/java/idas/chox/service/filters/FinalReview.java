package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.enums.FinalReviewMapping;

/**
 *
 * @author John
 */
public class FinalReview extends BaseFilter {
    private String status;
    private String name;
    private String key;


    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        //NB: null check added to getCurrentUser() to prevent error being thrown when user logd out before queues loaded
        if (securityInfoProvider.getCurrentUser() != null
                && securityInfoProvider.getCurrentUser().isCHOXAdmin()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.CHO_OR_INS_TRUE.getValue());
        } else if (securityInfoProvider.getCurrentUser() != null
                && securityInfoProvider.getCurrentUser().isCHO()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.CHO_TRUE.getValue());
        } else if (securityInfoProvider.getCurrentUser() != null
                && securityInfoProvider.getCurrentUser().isAnInsurer()) {
            claimSearchCriteria.setFinalReviewValue(FinalReviewMapping.INS_TRUE.getValue());
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
