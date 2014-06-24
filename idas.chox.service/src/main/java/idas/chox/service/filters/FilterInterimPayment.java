package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

/**
 *
 * @author John
 */
public class FilterInterimPayment extends BaseFilter {
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setIsInterimPaymentMade(true);

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
