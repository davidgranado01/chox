package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;
import idas.chox.core.search.ClaimSearchCriteria;

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
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        if (getCurrentUser().isCHOXAdmin()) {
            claimSearchCriteria.setFinalReviewIns(true);
            claimSearchCriteria.setFinalReviewCho(true);
        } else if (getCurrentUser().isCHO()) {
            claimSearchCriteria.setFinalReviewCho(true);
        } else {
            claimSearchCriteria.setFinalReviewIns(true);
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
