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
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCho, ClaimSearchCriteria claimSearchCriteria) {
//        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setInterimPaymentMade(true);

//        if (claimTypeId > -1) {
//            Set<ClaimType> claimTypes = new HashSet<ClaimType>();
//            claimTypes.add(ClaimType.values()[claimTypeId]);
//            claimSearchCriteria.setClaimTypes(claimTypes);
//        }

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
