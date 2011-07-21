package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class PenaltyChargesApplied extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(int insurerId, int choId) {
        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setIspenaltyChargeApplied(true);
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        if (insurerId > -1)
            claimSearchCriteria.setInsurerId(insurerId);
        if (choId > -1)
            claimSearchCriteria.setSupplierId(choId);

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
