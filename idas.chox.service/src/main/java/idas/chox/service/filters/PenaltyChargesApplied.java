package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class PenaltyChargesApplied extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCho, boolean paymentsTeamActive, ClaimSearchCriteria claimSearchCriteria) {
//        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setPenaltyChargeApplied(true);
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
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
