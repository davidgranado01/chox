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
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria, boolean paymentsTeamActive) {
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setInterimPaymentMade(true);

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
