package idas.chox.service.filters;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.search.ClaimSearchCriteria;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HireUpdateAnomalies extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setAnomalies(true);
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        Set<String> anomaliesStatus = new HashSet<String>(Arrays.asList(
                ClaimStatus.CLAIM_REF_TO_ENG,
                ClaimStatus.CLAIM_REFERRED_TO_FNOL,
                ClaimStatus.CLAIM_REJECTION_CONTESTED,
                ClaimStatus.CLAIM_PENDING,
                ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO,
                ClaimStatus.CLAIM_REJECTED,
                ClaimStatus.SUBSCRIBER_CLAIM_REJECTED,
                ClaimStatus.CLAIM_UPDATE_BY_ENG,
                ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED
        ));

        claimSearchCriteria.setStatuses(anomaliesStatus);

        return claimSearchCriteria;
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
