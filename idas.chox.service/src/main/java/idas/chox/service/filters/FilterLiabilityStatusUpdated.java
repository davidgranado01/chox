package idas.chox.service.filters;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.search.ClaimSearchCriteria;
import java.util.*;

public class FilterLiabilityStatusUpdated extends BaseFilter {
    private static Set<String> excludeList = new HashSet<String>();
    {
        excludeList.add(ClaimStatus.CLAIM_CLOSED);
        excludeList.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        excludeList.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        excludeList.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
    }

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(int insurerId, int choId) {

        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setIsShowOpenClaimsOnly(true);
        claimSearchCriteria.setLiabilityStatusUpdated(true);
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        claimSearchCriteria.setStatusExcludeList(excludeList);
        if (insurerId > -1)
            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
        if (choId > -1)
            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));

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
