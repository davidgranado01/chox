package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author John
 */
public class FinalReview extends BaseFilter {
    private String status;
    private String name;
    private String key;


    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, int insurerId, int choId) {
        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        if (insurerId > -1) {
            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
        }
        if (choId > -1) {
            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
        } 

        if (isCHO == null) {
            claimSearchCriteria.setFinalReviewIns(Boolean.TRUE);
            claimSearchCriteria.setFinalReviewCho(Boolean.TRUE);

        } else if (isCHO) {
            claimSearchCriteria.setFinalReviewCho(Boolean.TRUE);

        } else {
            claimSearchCriteria.setFinalReviewIns(Boolean.TRUE);
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
