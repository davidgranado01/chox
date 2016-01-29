package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class ClaimsRequiringAudit extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        
        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setClaimAuditValue(1);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

//        Set<String> claimsRequiringAuditStatus = new HashSet<>(Arrays.asList(
//                ClaimStatus.INVOICE_PAYMENT_RECEIVED,
//                ClaimStatus.MANUAL_INVOICE_PAID
//        ));
//
//        claimSearchCriteria.setStatuses(claimsRequiringAuditStatus);

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
