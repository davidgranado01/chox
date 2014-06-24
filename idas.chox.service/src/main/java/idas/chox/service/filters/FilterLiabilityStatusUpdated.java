package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class FilterLiabilityStatusUpdated extends BaseFilter {
//    private static final Set<String> excludeList = new HashSet<String>(4);
//    static {
//        excludeList.add(ClaimStatus.CLAIM_CLOSED);
//        excludeList.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
//        excludeList.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
//        excludeList.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
//    }

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCho, boolean paymentsTeamActive, ClaimSearchCriteria claimSearchCriteria) {

//        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setLiabilityStatusUpdated(true);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        // unnecessary as this is done when setShowOpenClaimsOnly to true.
//        claimSearchCriteria.setStatusExcludeList(excludeList);
//        if (insurerId > -1) {
//            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
//        }
//        if (choId > -1) {
//            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
//        }
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
